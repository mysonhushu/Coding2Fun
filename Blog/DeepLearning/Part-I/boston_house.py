import tensorflow as tf
import tensorflow.contrib.learn as tflearn
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

feature_columns = [tf.contrib.layers.real_valued_column("", dimension=X_train.shape[1])]

# optimizer = tf.train.RMSPropOptimizer(learning_rate=0.001, momentum=0.1)

# optimizer = tf.train.MomentumOptimizer(learning_rate=0.0001, use_nesterov=True, momentum=0.0)

regressor = tflearn.DNNRegressor(
    model_dir='boston/',
    activation_fn=tf.nn.elu,
    # optimizer=optimizer,
    config=tf.contrib.learn.RunConfig(save_checkpoints_secs=5),
    feature_columns=feature_columns,
    hidden_units=[50, 25, 10])

validation_monitor = tf.contrib.learn.monitors.ValidationMonitor(
    X_test,
    Y_test,
    every_n_steps=500,
    early_stopping_metric="loss",
    early_stopping_metric_minimize=True,
    early_stopping_rounds=50)

regressor.fit(X_train, Y_train, monitors=[validation_monitor], steps=10000)

tf.logging.set_verbosity(tf.logging.ERROR)

prediction = list(regressor.predict(X_test, as_iterable=True))
score = metrics.mean_squared_error(Y_test, prediction)

print("Final score (MSE): {}".format(score))

score = np.sqrt(metrics.mean_squared_error(prediction, Y_test))
print("Final score (RMSE): {}".format(score))

chart_regression(prediction, Y_test)
