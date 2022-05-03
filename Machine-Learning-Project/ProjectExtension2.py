#Machine Learning Project
#Author: Param Tully
#Date: Nov 30, 2020

#Project Extension 2

#User Inputs Data then gets desired result 

#import modules used in this program
from sklearn.linear_model import LinearRegression
import turtle

#get user info
print("What is the name of the file you would like to use.")
user_file = input("(Include ending):")

print(user_file)
#open user's file
file_user = open(user_file)

#get rid of header if present by asking user
user_header = input("Does the file have a header(y/n):").strip(" !.,").lower()
if user_header == "y":
  file_user.readline()

#ask user what what values to use as input and output
print("Enter positions for each input variable separanted only by commas")
user_input = input("(First Position is 0):").strip().split(",")
print("Enter position of one desired output variable:")
user_output = int(input("(First Position is 0):").strip())


#define input and output lists
input_list = []
output_list = []

#ask user what sign separates each datapoint in the file
#if they dont know, default to comma
user_symbol = input("Entry Symbol dividing each data point or 'idk' if don't know:").lower()
if user_symbol == "idk":
  user_symbol = ","

#make input and output lists
for line in file_user:

  #define line in file as list of values
  data_entry = line.strip().split(user_symbol)

  #define variable to hold desired input values
  input_values = []

  #withdraw inputs and outputs from each line
  if '' not in data_entry:
    for i in user_input:
      float_value = float(data_entry[int(i)])
      input_values.append(float_value)
  
    #add list of input values to input list and output values to output list
    input_list.append(input_values)
    output_list.append(data_entry[user_output])

#print(len(input_list))
#print(len(output_list))


#separate input and output lists into training lists and testing lists
training_input = input_list[0 : int(len(input_list)* 0.8)]
training_output = output_list[0 : int(len(output_list)* 0.8)]
testing_input = input_list[int(len(input_list)* 0.8):]
testing_output = output_list[int(len(output_list)* 0.8):]

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

#ask user for graph title and name of axes
user_x = input("What would you like to name the x-axis?:")
user_y = input("What would you like to label the y-axis?:").strip().split(" ")
user_title = input("What would you like to title the graph?:")


#find largest value percentage Error value ro dfine coordinates
max_value = 0

for v in percentErrorDic.values():
  if v > max_value:
    max_value = v


#set boundaries for graph
wn.setworldcoordinates(-100, - (max_value) //8, 40 * len(percentErrorDic) + 30, max_value * 6/5)

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
alexa.pensize(6)

#x axis
alexa.penup()
alexa.setposition(len(percentErrorDic) //2 * 40 - (len(user_x)) - 70, - (max_value) // 9 )
alexa.pendown()
alexa.write(user_x)

#yaxis
j = 0
for i in user_y:
  alexa.penup()
  alexa.setposition( -70, max_value //2 - (max_value * 1/10 * j))
  alexa.pendown()
  alexa.write(i)
  j += 1

#title
alexa.pensize(9)
alexa.penup()
alexa.setposition(len(percentErrorDic) //2 * 40 - (len(user_title)) - 70, max_value * 5.5 / 5)
alexa.pendown()
alexa.write(user_title)






  


