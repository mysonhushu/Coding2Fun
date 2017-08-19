import numpy as np
import tensorflow as tf
import tensorflow.contrib.learn as tflearn
from sklearn import datasets
from sklearn import metrics
from sklearn import model_selection
from sklearn import preprocessing

boston = datasets.load_boston()
x, y = boston.data, boston.target

# Split dataset into train / test
x_train, x_test, y_train, y_test = model_selection.train_test_split(x, y, test_size=0.25, random_state=42)

# Preprocessing
scaler = preprocessing.StandardScaler()
x_train = scaler.fit_transform(x_train)
x_test = scaler.transform(x_test)

# Input_fn
train_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': x_train}, y=y_train, batch_size=1, num_epochs=None,
                                                    shuffle=True)
test_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': x_test}, y=y_test, num_epochs=1, shuffle=False)

# Define the model with Estimator
feature_columns = [tf.feature_column.numeric_column('x', shape=np.array(x_train).shape[1:])]

# regressor = tflearn.LinearRegressor(feature_columns=feature_columns, model_dir='linear_reg/')

regressor = tflearn.DNNRegressor(feature_columns=feature_columns, hidden_units=[13, 10], model_dir='dnn_reg')

# Train.
regressor.fit(input_fn=train_input_fn, steps=10000)

# Predict
predictions = regressor.predict(input_fn=test_input_fn)
y_predictions = []
for _, p in enumerate(predictions):
    y_predictions.append(p)
# Score with sklearn.
score_sklearn = metrics.mean_squared_error(y_predictions, y_test)
print('MSE (sklearn): {0:f}'.format(score_sklearn))
