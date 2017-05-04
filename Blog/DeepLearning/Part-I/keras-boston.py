import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
import sklearn.metrics as metrics
from utils import encode_numeric_zscore, to_xy

dataset = pd.read_csv('data/boston-housing.csv', dtype=np.float32)

for col in dataset.columns:
    if col != 'medv':
        encode_numeric_zscore(dataset, col)

features, target = to_xy(dataset, 'medv')

X_train, X_test, Y_train, Y_test = train_test_split(features, target, test_size=0.25, random_state=42)

def chart_regression(pred, y):
    t = pd.DataFrame({'pred': pred, 'y': Y_test.flatten()})
    t.sort_values(by=['y'], inplace=True)
    a = plt.plot(t['y'].tolist(), label='expected')
    b = plt.plot(t['pred'].tolist(), label='prediction')
    plt.ylabel('output')
    plt.legend()
    plt.show()

from tensorflow.contrib.keras.api.keras.models import Sequential
from tensorflow.contrib.keras.api.keras.layers import Dense, Dropout

def build_nn():
    model = Sequential()
    model.add(Dense(50, input_shape=(13,), activation="relu", kernel_initializer="glorot_uniform"))
    model.add(Dense(20, activation="relu"))
    model.add(Dropout(0.2))
    model.add(Dense(10, activation="relu"))
    model.add(Dense(1, kernel_initializer="normal"))
    model.compile(loss='mean_squared_error', optimizer='adam')
    return model

seed = 7
np.random.seed(seed)

# Evaluate model (kFold cross validation)
from tensorflow.contrib.keras.api.keras.wrappers.scikit_learn import KerasRegressor

regressor = KerasRegressor(build_fn=build_nn, nb_epoch=100, batch_size=5, verbose=0)

regressor.fit(X_train, Y_train)

prediction = regressor.predict(X_test)

score = metrics.mean_squared_error(Y_test, prediction)

print("Final score (MSE): {}".format(score))

score = np.sqrt(metrics.mean_squared_error(prediction, Y_test))
print("Final score (RMSE): {}".format(score))

chart_regression(prediction, Y_test)

from sklearn.model_selection import cross_val_score
from sklearn.model_selection import KFold

kfold = KFold(n_splits=10, random_state=seed)
results = cross_val_score(regressor, X_train, Y_train, cv=kfold)

print('Baseline: %.2f (%.2f) MSE' % (results.mean(), results.std()))