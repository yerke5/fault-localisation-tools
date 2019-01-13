import numpy as np
import matplotlib.pyplot as plt

# Visual Studio
class NN:
    def __init__(self, inputSize, hiddenSize, outputSize, seed=11):
        # params
        self.iSize = inputSize
        self.hSize = hiddenSize
        self.oSize = outputSize

        # levels
        self.iLevel = np.zeros([inputSize])
        self.hLevel = np.zeros([hiddenSize])
        self.oLevel = np.zeros([outputSize])

        # synapses
        self.ihSyn = 2 * np.random.random([inputSize, hiddenSize]) - 1
        self.hoSyn = 2 * np.random.random([hiddenSize, outputSize]) - 1

        # biases
        self.hBias = 2 * np.random.random([hiddenSize]) - 1
        self.oBias = 2 * np.random.random([outputSize]) - 1

        # seed
        np.random.seed(seed)

    def sigmoid(self, x):
        return 1 / (1 + np.exp(-x))

    '''def tanh(self, x):
        return (x < -20) ? -1 : (x > 20 ? 1 : math.tanh(x))'''

    def forward(self, x):
        hVal = np.zeros([self.hSize])
        oVal = np.zeros([self.oSize])

        # copy values from current test case to input nodes
        for i in range(self.iSize):
            self.iLevel[i] = x[i]

        # multiply IH weights by inputs
        for j in range(self.hSize):
            for i in range(self.iSize):
                hVal[j] += self.iLevel[i] * self.ihSyn[i][j]
            hVal[j] += self.hBias[j]
            self.hLevel[j] = self.sigmoid(hVal[j])

        # multiply HO weights by hidden layer values
        for j in range(self.oSize):
            for i in range(self.hSize):
                oVal[j] += self.hLevel[i] * self.hoSyn[i][j]
            oVal[j] += self.oBias[j]
            self.oLevel[j] = self.sigmoid(oVal[j])
        
        return self.oLevel
        

    def train(self, trainX, trainY, params):
        numIterations = params["numTrainings"]
        learningRate = params["learningRate"]
        adj = params["lambdaL2"]
        l2 = params["l2"]
        printErrors = params["printErrors"]
        printEveryIter = params["printEveryIter"]
        plotErrors = params["plotErrors"]
        plotEveryIter = params["plotEveryIter"]
        dHO = np.zeros([self.hSize, self.oSize]) # hidden-to-output gradients
        dOB = np.zeros([self.oSize]) # output-level biases
        dIH = np.zeros([self.iSize, self.hSize]) # input-to-hidden gradients
        dHB = np.zeros([self.hSize]) # hidden-level biases

        lO = np.zeros([self.oSize])
        lH = np.zeros([self.hSize])

        numTests = trainX.shape[0]
        errors = np.zeros([int(numIterations / plotEveryIter)])

        #x = np.zeros([trainX.shape[1]])

        for trainIter in range(numIterations):
            for testCase in range(numTests):
                x = trainX[testCase]
                y = trainY[testCase]
                self.forward(x) # get output for current test case

                # 1. ERROR CALCULATION

                # 1.1 calculate output gradients ((dOut / dNet) * (dError / dOut) * hidden)
                for i in range(self.oSize):
                    oError = self.oLevel[i] - y[i] # out - target
                    oDeriv = self.oLevel[i] * (1 - self.oLevel[i]) # dOut / dNet
                    lO[i] = oError * oDeriv
                
                for i in range(self.hSize):
                    for j in range(self.oSize):
                        dHO[i][j] = lO[j] * self.hLevel[i]
                        if(l2):
                            dHO[i][j] += adj * self.hoSyn[i][j]

                # 1.2. calculate output bias gradients
                for i in range(self.oSize):
                    dOB[i] = lO[i]

                # 1.3. calculate hidden layer weight gradients
                for i in range(self.hSize):
                    s = 0 # dEtotal / dOutH[i]
                    for j in range(self.oSize):
                        s += lO[j] * self.hoSyn[i][j] # weight * activated output ((dEO / dNetO) * (dNetO / dOutH))
                    hDeriv = (1 - self.hLevel[i]) * (0 + self.hLevel[i]) # change later
                    lH[i] = hDeriv * s

                for i in range(self.iSize):
                    for j in range(self.hSize):
                        dIH[i][j] = lH[j] * self.iLevel[i]
                        if(l2):
                            dIH[i][j] += adj * self.ihSyn[i][j]

                # 1.4. calculate hidden layer bias gradients
                for i in range(self.hSize):
                    dHB[i] = lH[i]

                # 2. WEIGHT ADJUSTMENT

                # 2.1. update input-to-hidden weights
                for i in range(self.iSize):
                    for j in range(self.hSize):
                        self.ihSyn[i][j] -= learningRate * dIH[i][j]

                # 2.2. update hidden biases
                for i in range(self.hSize):
                    self.hBias[i] -= learningRate * dHB[i]

                # 2.3. update hidden-to-output weights
                for i in range(self.hSize):
                    for j in range(self.oSize):
                        self.hoSyn[i][j] -= learningRate * dHO[i][j]

                # 2.4. update output biases
                for i in range(self.oSize):
                    self.oBias[i] -= dOB[i] * learningRate
                
            if plotErrors and (1 + trainIter) % plotEveryIter == 0:
                error = self.getError(trainX, trainY)
                errors[int(trainIter / plotEveryIter)] = error
            if printErrors and (1 + trainIter) % printEveryIter == 0:
                error = self.getError(trainX, trainY)
                print("Error for iteration #" + str((1 + trainIter)) + ": %0.4f" % error)
        if(plotErrors):
            self.plotErrors(errors, learningRate, params["programName"])
        return self.oLevel

    def getError(self, inp, target):
        total = 0
        x = np.zeros([self.iSize])
        t = np.zeros([self.oSize])

        for i in range(len(inp)):
            x = inp[i]
            t = target[i]
            y = self.forward(x)
            for j in range(self.oSize):
                e = t[j] - y[j]
                total += e * e
        return total / len(inp)

    def plotErrors(self, errors, lr, programName, plotFile="errors.png"):
        plt.plot(errors, label=programName)
        plt.ylabel('Errors')
        plt.xlabel('Iterations x 100')
        plt.title("Learning rate = " + str(lr))
        plt.legend()
        plt.savefig(plotFile)

def getInput(m):
    if m == 2:
        return np.array([
            [1, 0, 0, 0, 1, 1, 0, 1, 1, 1],
            [1, 1, 0, 1, 0, 0, 0, 0, 1, 0],
            [1, 0, 1, 0, 0, 1, 1, 1, 1, 1],
            [1, 1, 1, 1, 0, 0, 1, 1, 1, 0],
            [1, 1, 1, 1, 0, 1, 1, 1, 1, 0],
            [1, 0, 0, 0, 0, 0, 1, 1, 1, 1],
            [1, 1, 0, 0, 0, 1, 1, 0, 1, 1],
            [1, 1, 1, 1, 0, 0, 1, 1, 1, 0],
            [0, 1, 0, 0, 0, 1, 0, 0, 1, 1],
            [1, 0, 0, 0, 1, 0, 1, 1, 1, 1],
            [0, 1, 1, 0, 0, 0, 0, 1, 1, 0],
            [1, 1, 0, 1, 1, 0, 1, 0, 1, 1],
            [1, 0, 1, 0, 0, 0, 1, 1, 1, 0],
            [0, 1, 0, 1, 1, 0, 1, 0, 1, 1],
            [1, 0, 1, 0, 1, 0, 1, 1, 1, 1],
            [0, 1, 0, 1, 0, 0, 0, 1, 1, 0],
            [1, 1, 1, 1, 0, 0, 1, 1, 1, 0],
            [1, 0, 1, 0, 1, 1, 0, 1, 1, 1],
            [1, 1, 1, 1, 0, 0, 1, 0, 1, 0],
            [0, 0, 1, 1, 1, 1, 1, 0, 1, 1],
            [1, 1, 0, 0, 0, 0, 1 ,1, 0, 1],
            [0, 0, 0, 1, 1, 0, 1, 1, 0, 0],
            [0, 1, 1, 0, 0, 0, 1, 0, 0, 1],
            [1, 1, 1, 0, 1, 0, 0, 0, 0, 1],
            [1, 0, 0, 1, 1, 1, 0, 1, 1, 0],
            [1, 1, 1, 0, 0, 1, 0, 0, 0, 1],
            [1, 0, 0, 0, 0, 1, 1, 0, 1, 1],
            [0, 1, 1, 1, 1, 0, 0, 1, 0, 0],
            [0, 0, 1, 1, 1, 1, 0, 1, 0, 0],
            [1, 0, 0, 0, 1, 1, 0, 0, 1, 1],
            [1, 1, 0, 1, 0, 0, 0, 1, 0, 0],
            [1, 0, 0, 1, 1, 1, 1, 0, 0, 1],
            [1, 1, 0, 0, 0, 1, 0, 1, 0, 0],
            [1, 0, 1, 1, 1, 0, 0, 1, 1, 0],
            [0, 0, 0, 1, 1, 0, 1, 1, 1, 0],
            [0, 0, 1, 0, 1, 0, 0, 0, 1, 0]
        ])
    elif m == 1:
        return np.array([
            [1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1],
            [1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1],
            [1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1],
            [1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1],
            [1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1],
            [1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1]
        ])
    elif m == 3:
        return np.array([
            [1,1,1,1,0,1,0,0,1],
            [1,0,0,0,1,1,1,1,0],
            [0,0,0,0,0,1,1,0,0],
            [1,1,0,0,1,0,1,1,1],
            [1,1,1,0,1,1,1,1,1],
            [0,0,1,0,0,1,1,1,0],
            [1,1,1,1,0,1,0,1,1]
        ])
    else:
        print("Error getting sample input")

def getTargetOutput(m):
    if m == 2:
        return np.array([
            [1], [0], [1], [0], [0], [0], 
            [0], [0], [0], [1], [0], [0], 
            [0], [0], [1], [0], [0], [1],
            [0], [0], [1], [0], [0], [0],
            [1], [0], [0], [0], [0], [0],
            [1], [0], [0], [1], [0], [0]
        ])
    elif m == 1:
        return np.array([[0], [0], [0], [0], [0], [1]])
    elif m == 3:
        return np.array([[0],[0],[0],[0],[0],[1],[1]])
    else:
        print("Error getting target output")

def getTrainData(trainFileName):
    file = open(trainFileName, "r")
    content = file.readlines()
    x = []
    y = []
    for line in content:
        x.append(list(map(int, line[:-4].split(" "))))
        y.append([int(line[-3])])
    return np.array(x), np.array(y)
        

def getTestData(numStms):
    x = np.zeros((numStms, numStms))
    for i in range(numStms):
        x[i][i] = 1
    return x

def printTrainParams(params):
    print("Training Parameters".upper())
    for pair in params:
        print(pair + ":\t" + str(params[pair]))

def forward(model, x):
    print("Forward propagation outputs".upper())
    res = {}
    for i in range(len(x)):
        y = model.forward(x[i])
        res.update({(i + 1):y[0]})
    sortedRes = sorted(res.items(), key=lambda kv: kv[1], reverse=True)
    for r in sortedRes:
        print(r[0], "\t", r[1])

def run(programName, trainFileName):
    # get training data
    trainX, trainY = getTrainData(trainFileName)
	
    # set model parameters
    numStms = trainX.shape[1]
    iSize = numStms
    hSize = 5
    oSize = 1
    model = NN(iSize, hSize, oSize)
    params = {"programName": programName, "numHiddenNodes": hSize, "learningRate": 0.5, "numTrainings": 4000, "lambdaL2": 0.005, "l2": False, "printEveryIter": 1000, "printErrors": True, "plotEveryIter": 100, "plotErrors": True}
    
    # start training
    print(programName, "WITH", trainX.shape[0], "TEST CASES")
    print(">>> Starting model training...\n")
    printTrainParams(params)
    if params["printErrors"]:
        print("\nERRORS")
    model.train(trainX, trainY, params)
	
    # start testing
    testData = getTestData(numStms)
    print("\n")
	
	# get suspiciousness scores
    forward(model, testData)
    print("___________________________________")

def printIdentityMatrix():
    t = np.zeros([10, 10])
    for i in range(10):
        t[i][i] = 1
    print(t)

if __name__ == "__main__":
    # please note that the larger the matrix, the longer it will take for the model to run
    # even for these 3 matrices, training will take a while
    
    run("YEAR INFO", "wrongYear10.txt") # program implemented by us
    run("CROSSTAB", "crosstab.txt") # coverage matrix from the paper about Crosstab
    run("TARANTULA", "tarantula.txt") # coverage matrix from the paper about Tarantula
