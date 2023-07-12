#
# Copyright (C) 2015 Ozan Oktay, Jonathan Passerat-Palmbach
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#

import matplotlib
matplotlib.use('Agg')  # to be imported before pyplot
import matplotlib.pyplot as plt
from sklearn import datasets, metrics
from sklearn.ensemble import RandomForestClassifier
from PIL import Image
import os
import numpy as np
import random
from sklearn.model_selection import cross_val_score
from skimage.feature import hog
from skimage import data, color, exposure
import joblib
import sys


dir_images  = sys.argv[1]
# best example of the most inconsistent language ever:
# claims everywhere that is abstracts its dev from types
# but crashes if you don't force cast these strings as ints!!!
num_trees   = int(sys.argv[2])
trees_depth = int(sys.argv[3])

name_images = []

# READ THE IMAGE NAMES
for root, dirs, files in os.walk(dir_images):
    for file in files:
        if file.endswith(".jpg"):
            name_images.append(root+'/'+file)
name_images = sorted(name_images)

# READ THE IMAGES
image_np = []
features = []
for name_image in name_images:
    image_loaded = Image.open(name_image).convert("L").resize([100,100])
    image_np.append(np.array(image_loaded))

    fd, hog_image = hog(image_loaded, orientations=8, pixels_per_cell=(16, 16),cells_per_block=(1, 1), visualize=True)
    features.append(hog_image.flatten())


# GENERATE THE LABELS FOR THE IMAGES
labels = np.zeros (len(image_np))
labels [:65]    = 0
labels [66:125] = 1
labels [126:]   = 2

# The digits dataset
image_np = np.array(image_np)
features = np.array(features)

# random shuffle the arrays based on their indices
indices  = np.random.permutation(labels.shape[0])
labels   = labels[indices]
features = features[indices]
image_np = image_np[indices]

images_and_labels = list(zip(image_np, labels))

#######################################
# HERE GOES THE CLASSIFIER TRAINING ##

fig = plt.figure()
fig.set_size_inches(10, 8)

for index, (image, label) in enumerate(images_and_labels[:12]):
    subplt = fig.add_subplot(2, 12, index + 1)
    subplt.axis('off')
    subplt.imshow(image, cmap=plt.cm.gray_r, interpolation='nearest')
    subplt.set_title('Training: %i' % label)

# To apply a classifier on this data, we need to flatten the image, to
# turn the data in a (samples, feature) matrix:
n_samples = image_np.shape[0]
data      = image_np.reshape( (n_samples, -1) )
data      = np.concatenate( (data, features), axis=1)
#print data.shape

# Create a classifier: a support vector classifier
#classifier = svm.SVC(gamma=0.001) # here goes the parameters
classifier = RandomForestClassifier(max_depth=trees_depth, n_estimators=num_trees, max_features='auto', criterion='entropy')

# Perform a cross-validation
#scores = cross_val_score(classifier, data, labels, cv=10)
#print 'cross validation result: %f' % scores.mean()

# We learn the leaves on the first 80% of the dataset
training_share = int(4 * n_samples / 5)
classifier.fit(data[:training_share], labels[:training_share])

# Export the trained classifier
#joblib.dump(classifier, 'my_model.pkl', compress=9)

# Now predict the value of the digit on the second half:
#print 'number of samples: {0}'.format(n_samples)

expected  = labels[training_share:]
predicted = classifier.predict(data[training_share:])

#print("Classification report for classifier %s:\n%s\n"
#      % (classifier, metrics.classification_report(expected, predicted)))
#print("Confusion matrix:\n%s" % metrics.confusion_matrix(expected, predicted))

images_and_predictions = list(zip(image_np[training_share:], predicted))

for index, (image, prediction) in enumerate(images_and_predictions[:12]):
    subplt = fig.add_subplot(2, 12, index + 13)
    subplt.axis('off')
    subplt.imshow(image, cmap=plt.cm.gray_r, interpolation='nearest')
    subplt.set_title('Prediction: %i' % prediction)

fig.savefig('summary.png')

# accuracy
print("%f" % metrics.accuracy_score(expected, predicted) )

# Convert array to Image
#img = PIL.Image.fromarray(arr)

# deserialize classifier
# classifier = joblib.load('my_model.pkl')
