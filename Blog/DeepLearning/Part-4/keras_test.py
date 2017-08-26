import numpy as np

import pandas as pd

import keras.backend as K
from keras import Input
from keras import layers
from keras.models import Model
from keras.preprocessing import sequence
from keras.preprocessing.text import Tokenizer
from keras.utils import to_categorical

import tensorflow as tf

text_col = 'consumer_complaint_narrative'
target_col = 'product'

data_set = pd.read_csv('dataset/complaints.csv')

features = data_set[text_col].values
targets = data_set[target_col].values

max_features = 5000
maxlen = 400
batch_size = 32
embedding_dims = 50
filters = 250
kernel_size = 3
hidden_dims = 250

targets = to_categorical(targets, 11)

tokenizer = Tokenizer(num_words=max_features)
tokenizer.fit_on_texts(features)

features_seq = tokenizer.texts_to_sequences(features)
word_index = tokenizer.word_index

from sklearn.model_selection import train_test_split


X_train, X_test, y_train, y_test = train_test_split(features_seq, targets, random_state=55, test_size=0.20)


X_train = sequence.pad_sequences(X_train, maxlen=maxlen, padding='post')
X_test = sequence.pad_sequences(X_test, maxlen=maxlen, padding='post')

print('X_train - ',np.shape(X_train))
print('X_test - ',np.shape(X_test))

input_layer = Input(shape=(X_train.shape[1], ))
embedding = layers.Embedding(max_features, embedding_dims, input_length=maxlen)(input_layer)
# bid = layers.Bidirectional(layers.LSTM(64))(embedding)
drop_out = layers.Dropout(0.2)(embedding)
conv1 = layers.Conv1D(filters, kernel_size, padding='valid', activation='relu', strides=1)(drop_out)
m_pool = layers.GlobalMaxPooling1D()(conv1)
dense = layers.Dense(hidden_dims)(m_pool)
drop = layers.Dropout(0.2)(dense)
rel = layers.Activation('relu')(drop)
prediction = layers.Dense(11, activation='softmax')(rel)
model = Model(inputs=input_layer, outputs=prediction)
print(model.summary())

model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['acc'])

model.fit(X_train, y_train, epochs=10, batch_size=batch_size, validation_data=(X_test, y_test), verbose=1)
