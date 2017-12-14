# Decision Trees
This project build a simple decision trees from Titanic Dataset. It also contains an implementation of Weka Api.
You can find here: 
•	Main classes needed to represent the dataset.
•	Connecting the database and extracting the dataset.
•	Methods to build decision tree with one characteristic. 
•	Integrating Weka C4.5 algorithms for building decision tree with multi-characteristics. 

# Platform & Technologies Used 
-	Java 8 as a programming language on NetBeans 8.2 platform. 
-	Weka API 3.7.7 java library that gives us the ability to build C4.5 Decision Tree using J48 functions.

# MVC Architecture 
-	Controllers Package: contain the main controller for this application.
-	Views Package: contain the jFrames of the application
-	Models: contains all java classes, types, and enums need to represent the data.
-	Helpers: contain extra helper classes.

# Classes 
## Dataset, Characteristics, Rows, and Values 
We have couple of classes that represent the data in the database, described as following: 
1. Characteristic Class
2. Row Class
3. Dataset Class,	Contain two main lists,	List of Characteristics ,List of Rows

# Integrating Weka and Generating the Instances 

In this application, there are two main parts related to Weka Api, the first one which responsible for building Decision tree using C4.5 Algorithm which is implemented in Weka Api through J48 object, and the second part responsible for evaluating the generated j48 classifier with our testing dataset.

## 	Building Decision tree using C4.5 algorithm 
There are two ways for build the decision tree using Weka, either by using dataset loaded from “.arff” file or building the Weka dataset manually by looping our training dataset which what we did here in this method:

```public WekaHelperModel createDecisionTreeC45() ```

### The first step in this method is to create vector containing all attributes (our characteristics). 

```FastVector attributesList = new FastVector();```

### The second is to loop all our training rows and create a list of instances along with the corresponding attributes.

```Instances trainDataSet = new Instances("TrainingDataSet",attributesList, this.rows.size());```

### After these two operation we will have to build the decision tree by passing our trainDataSet to the J48 classifier using the following code:

```
J48 cls = new J48();
cls.buildClassifier(trainDataSet);
return new WekaHelperModel(trainDataSet, cls);
```

This method return object of “WekaHelperModel” which contain two members: the “trainDataSet” and J48 classifier, which is important for the next step “Evaluating”.





## 	Evaluating C4.5 Decision Tree
In the last step, we build the C4.5 Decision Tree, now, we will need to evaluate the tree using the loaded test dataset. And again, the first step will be generating the Weka Test Dataset in the same wat that we generate the train dataset using the method “generateWekaTestingDataSet” in the class “TestingDataSet”, by looping the characteristics and rows to create a list of attributes and list of instances which are accepted by Weka Api. 

```public Instances generateWekaTestingDataSet```

After we build the Weka Test Dataset, a method called “evaluateWekaModel” in the class “DataSet” will be responsible of evaluation process, and obviously the input for this method is the wekaModel and testDataSet that we generated in the previous steps:

```public String evaluateWekaModel(WekaHelperModel wekaModel, Instances testDataSet)```

Then we have just to use the following code to evaluate the tree:

```Evaluation et = new Evaluation(wekaModel.getTrainDataSet());
et.evaluateModel(wekaModel.getClassifier(), testDataSet);
return eTest.toSummaryString();
```

This method is returning the summary (String) of evaluation which contain the number of correctly/incorrectly classified instances, mean absolute error, and other measures. 


