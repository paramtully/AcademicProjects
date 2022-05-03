#Machine Learning Project
#Author: Param Tully
#Date: Nov 30, 2020

#Project Extension 1

#perdicting total likes with all other variables as inputs 

#step 1: Processing the Data

#import modules used in this program
from sklearn.linear_model import LinearRegression
import turtle

#open file
file_park = open("dataset_Facebook.csv")

#get rid of header
file_park.readline()

#define input and output lists
input_list = []
output_list = []

#make input and output lists
for line in file_park:

  #define line in file as list of values
  data_entry = line.strip().split(";")

  #define variable to hold desired input values
  input_values = []

  #withdraw inputs and outputs from each line
  if '' not in data_entry:
    for i in data_entry[2:]:
      float_value = float(i)
      input_values.append(float_value)
  
    #add list of input values to input list and output values to output list
    input_list.append(input_values)
    output_list.append(data_entry[0])



#separate input and output lists into training lists and testing lists
training_input = input_list[0 : int(len(input_list)* 0.8)]
training_output = output_list[0 : int(len(output_list)* 0.8)]
testing_input = input_list[int(len(input_list)* 0.8):]
testing_output = output_list[int(len(output_list)* 0.8):]

#step 2: Train Your Model

#build model by giving module training lists
predictor = LinearRegression(n_jobs = -1)
predictor.fit(X=training_input, y=training_output)

#Step 3: Using your model for prediction

#input testing list to get outputs then compare to actual
X_likes = testing_input
outcome = predictor.predict(X = X_likes)
outcome_list = list(outcome)
coefficients = predictor.coef_

#print each predicted outcome and corresponding actual outcome 
#for i in range(len(outcome_list)):
  #print("Predicted:", int(outcome_list[i]), "Actual:", int(testing_output[i]))


#Step 4: Visualizing the Performance of Your Model

#define variables to count each percentage interval
within10 = 0
within10to20 = 0
within20to30 = 0
within30to40 = 0
within40to50 = 0
within50to60 = 0
within60to70 = 0
within70to80 = 0
within80to90 = 0
within90to100 = 0
over100 = 0

#find percent error for each predicted output
for i in range(len(outcome_list)):
  #add condition excluding no business days
  if int(testing_output[i]) >= 1:
    actualValue = int(testing_output[i])
    predictedValue = int(outcome_list[i])

    percentageError = abs(actualValue - predictedValue) * 100 / actualValue 

    if percentageError < 10:
      within10 += 1
    elif percentageError >= 10 and percentageError < 20:
      within10to20 += 1
    elif percentageError >= 20 and percentageError < 30:
      within20to30 += 1
    elif percentageError >= 30 and percentageError < 40:
      within30to40 += 1
    elif percentageError >= 40 and percentageError < 50:
      within40to50 += 1
    elif percentageError >= 50 and percentageError < 60:
      within50to60 += 1
    elif percentageError >= 60 and percentageError < 70:
      within60to70 += 1
    elif percentageError >= 70 and percentageError < 80:
      within70to80 += 1
    elif percentageError >= 80 and percentageError < 90:
      within80to90 += 1
    elif percentageError >= 90 and percentageError < 100:
      within90to100 += 1
    else:
      over100 += 1


#add all items to dictionary
percentErrorDic = {}
percentErrorDic["within10"] = within10
percentErrorDic["within10to20"] = within10to20
percentErrorDic["within20to30"] = within20to30
percentErrorDic["within30to40"] = within30to40
percentErrorDic["within40to50"] = within40to50
percentErrorDic["within50to60"] = within50to60
percentErrorDic["within60to70"] = within60to70
percentErrorDic["within70to80"] = within70to80
percentErrorDic["within80to90"] = within80to90
percentErrorDic["within90to100"] = within90to100
percentErrorDic["over100"] = over100


#initialize turtle
alexa = turtle.Turtle()
wn = turtle.Screen()

#find largest value percentage Error value ro dfine coordinates
max_value = 0

for v in percentErrorDic.values():
  if v > max_value:
    max_value = v


#set boundaries for graph
wn.setworldcoordinates(-100, -20, 40 * len(percentErrorDic) + 20 + 10, max_value + 20)

#function to make bars of graph and label
def bar(height):
  #alexa.write(str(interval))
  alexa.begin_fill()
  alexa.left(90)
  alexa.forward(height)
  alexa.right(90)
  alexa.forward(6)
  alexa.write(str(height))
  alexa.forward(34)
  alexa.right(90)
  alexa.forward(height)
  alexa.left(90)
  alexa.end_fill()

#set chart colour and pen size
alexa.fillcolor("blue")
alexa.pensize(4)

#draw graph whle labeling max values
for (k,v) in percentErrorDic.items():
  bar(v)

#label graph
#x axis
alexa.pensize(6)
alexa.penup()
alexa.setposition(-60, -5)
alexa.pendown()
alexa.write("10% Intervals of Percentage Error from 0 to 100 and Over 100")
#y axis
alexa.penup()
alexa.setposition(-90, max_value //2)
alexa.pendown()
alexa.write("Number")
alexa.penup()
alexa.setposition(-90, max_value //2 - 5)
alexa.pendown()
alexa.write("Outputs")
#title
alexa.pensize(9)
alexa.penup()
alexa.setposition(-95, max_value * 2 - 30)
alexa.pendown()
alexa.write("Number Outputs per 10% Percentage Error Interval or over 100 Graph")