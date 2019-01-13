BPNN:
	- you can change model parameters by changing values in the params dictionary
	- e.g. you can set the number of trainings to 8000 or increase the learning rate
	- the learning rate plot will be saved in a picture called "errors.png". If you run the model with several matrices, the curves will be printed in the same picture with legend
	- you can choose to not print the errors by setting printErrors to False
	- you can choose to not plot the errors by setting plotErrors to False
	- you can choose to turn on L2 regularisation by setting l2 to True (False by default)
	- all parameters mentioned above can be adjusted in the params dictionary on line 296
	- it takes a while for the model to run, especially with larger matrices
	- for formulas used in the source code, please refer to the report
	- NN is a class, so to create a new neural network, use, e.g. model = NN(...)
	- coverage matrices are read from files. By default, BPNN reads files "wrongYear10.txt", "crosstab.txt" and "tarantula.txt", but you can try using your own matrices. Please follow the format described in the paper
	- please make sure NumPy is installed before you run the program
	- please use an appropriate program, such as IDLE 3.6.4, to run the program (version 2.7 will not work)

Tarantula:
	- the faulty program is located in Tarantula's package and is called FaultyProgram.java
	- to get Tarantula outputs, please run FaultyProgram.java
	- the main module for Tarantula can be found at Tarantula.java
	- the source code explains considerations made during the calculation of suspiciousness scores
	- FaultyProgram also reads matrices from files, but you can generate new random test cases by calling generateData()
	- test inputs (years) are generated randomly and are between 1918 and 2118, to make sure future years are as likely to come as past years
	- the buggy program itself is in a method called coverage()
	- the buggy program currently has a bug at line 80 (!= should be ==)
	- for more details, please refer to the report
	- please use Eclipse to import the project and run the program