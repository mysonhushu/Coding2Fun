import numpy as np
import pandas as pd
import tensorflow as tf
from sklearn.model_selection import train_test_split
from tensorflow.contrib import learn
from tensorflow.contrib.keras.api.keras import layers
from tensorflow.contrib.keras.api.keras.preprocessing import sequence
from tensorflow.contrib.keras.api.keras.preprocessing.text import Tokenizer
from tensorflow.contrib.keras.api.keras.utils import to_categorical
from tensorflow.contrib.learn.python.learn.estimators import model_fn as model_fn_lib

tf.logging.set_verbosity(tf.logging.INFO)

max_features = 5000
maxlen = 400
batch_size = 32
embedding_dims = 50
filters = 250
kernel_size = 3
hidden_dims = 250

class_json = {0: 'Bank account or service',
              1: 'Consumer Loan',
              2: 'Credit card',
              3: 'Credit reporting',
              4: 'Debt collection',
              5: 'Money transfers',
              6: 'Mortgage',
              7: 'Other financial service',
              8: 'Payday loan',
              9: 'Prepaid card',
              10: 'Student loan'}

text = 'consumer_complaint_narrative'
target = 'product'


def model_fn(features, targets, mode, params):
    input_text = features['x']
    embedding = layers.Embedding(max_features, embedding_dims, input_length=maxlen)(input_text)
    drop_out = tf.layers.dropout(embedding, rate=0.2)
    conv1 = layers.Conv1D(filters, kernel_size, padding='valid', activation='relu', strides=1)(drop_out)
    m_pool = layers.GlobalMaxPooling1D()(conv1)
    dense = layers.Dense(hidden_dims)(m_pool)
    drop = tf.layers.dropout(dense, rate=0.2)
    rel = layers.Activation('relu')(drop)
    logits = layers.Dense(11)(rel)

    loss = None
    train_op = None

    if mode != tf.estimator.ModeKeys.PREDICT:
        loss = tf.losses.softmax_cross_entropy(onehot_labels=targets, logits=logits)

    if mode == tf.estimator.ModeKeys.TRAIN:
        train_op = tf.contrib.layers.optimize_loss(loss, tf.contrib.framework.get_global_step(), optimizer=tf.train.AdamOptimizer,
                                                   learning_rate=params['learning_rate'])

    predictions = {
        "classes": tf.argmax(input=logits, axis=1),
        "probabilities": tf.nn.softmax(logits)
    }

    eval_metric_ops = {
        "accuracy": tf.metrics.accuracy(
            tf.argmax(input=logits, axis=1),
            tf.argmax(input=targets, axis=1))
    }
    return model_fn_lib.ModelFnOps(mode=mode, predictions=predictions, loss=loss, train_op=train_op,
                                   eval_metric_ops=eval_metric_ops)


text_col = 'consumer_complaint_narrative'
target_col = 'product'

data_set = pd.read_csv('dataset/complaints.csv')

features = data_set[text_col].values
targets = data_set[target_col].values

targets = to_categorical(targets, 11)

tokenizer = Tokenizer(num_words=max_features)
tokenizer.fit_on_texts(features)

features_seq = tokenizer.texts_to_sequences(features)
word_index = tokenizer.word_index

X_train, X_test, y_train, y_test = train_test_split(features_seq, targets, random_state=55, test_size=0.20)

X_train = sequence.pad_sequences(X_train, maxlen=maxlen, padding='post')
X_test = sequence.pad_sequences(X_test, maxlen=maxlen, padding='post')

print('X - Train ', np.shape(X_train))
print('X - Test ', np.shape(X_test))
print('Y - Train', np.shape(y_train))
print('Y - Test', np.shape(y_test))

train_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X_train}, y=y_train, batch_size=32, num_epochs=None,
                                                    shuffle=True)
test_input_fn = tf.estimator.inputs.numpy_input_fn(x={'x': X_test}, y=y_test, num_epochs=1, shuffle=False)

model_params = {"learning_rate": 0.001}

estimator = learn.Estimator(model_fn=model_fn, params=model_params, model_dir='build/')
print("Training")
print('-' * 40)

estimator.fit(input_fn=train_input_fn, steps=1000)

print("Testing")
print('-' * 40)

evaluation = estimator.evaluate(input_fn=test_input_fn)
print("Loss: %s" % evaluation["loss"])
print("Accuracy: %f" % evaluation["accuracy"])
