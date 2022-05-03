#Machine Learning Project
#Author: Param Tully
#Date: Nov 30, 2020

#1 Building our First Machine Learning Model

#step 2: Generating a training set

#import modules used in this program
import random
from sklearn.linear_model import LinearRegression

#define list 3 integer lists and list of outputs
input_list = []
output_list = []

#make list of 3 integer lists
while len(input_list) < 100:

  #add 3 integer list of random numbers to input_list
  input_list.append([random.randint(0, 1000), random.randint(0, 1000), random.randint(0,1000)])

#make output list
for i in range(len(input_list)):

  #define variables to be plugged into the equation
  x = input_list[i][0]
  y = input_list[i][1]
  z = input_list[i][2]

  output_list.append(x + y*2 + z*3)

#step 3:   Training the Machine Learning Model
#set up model by feeding inputs and outputs
predictor = LinearRegression(n_jobs = -1)
predictor.fit(X = input_list, y = output_list)

#step 4:  Using out Model for Prediction

X_test = [[10,20,30]]
outcome = predictor.predict(X = X_test)
coefficients = predictor.coef_

print("Prediction:", outcome)
print("Coefficients:", coefficients)


