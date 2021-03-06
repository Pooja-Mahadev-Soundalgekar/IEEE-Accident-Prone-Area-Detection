import csv

class KModes:
    
    def __init__(self,z,k=8,verbose=0):
        self.data               = z
        self.k                  = k
        self.verbose            = verbose
        self.numobjects         = len(z)
        self.numattributes      = len(z[0])
        self.clustervalues      = [['' for x in range(self.numattributes)] for y in range(self.k)]
        self.clustership        = [-1 for y in range(self.numobjects)]
        self.clustercount       = [0 for y in range(self.k)]
        self.clusterfrequency   = [[{} for x in range(self.numattributes)] for y in range(self.k)]
        
        assert self.k < self.numobjects, "More clusters than data points?"
        
    def BuildInitialClusters(self, rand):
        # Choose random data values for the k clusters
        for i in range(self.k):
            self.clustervalues[i] = self.data[rand]
            
        totaldistance = 0
        for i in range(self.numobjects):
            closest = self.ClosestCluster(i)
            cluster = closest[0]
            totaldistance += closest[1]
            self.clustership[i] = cluster
            self.clustercount[cluster] += 1
            for j in range(self.numattributes):
                if self.data[i][j] in self.clusterfrequency[cluster][j].keys():
                    self.clusterfrequency[cluster][j][self.data[i][j]] += 1
                else:
                    self.clusterfrequency[cluster][j][self.data[i][j]] = 1
                self.clustervalues[cluster][j] = self.HighestFrequency(cluster,j)
                
        # DEBUG CODE
        if self.verbose > 0:
            print("C1: {}".format(self.clustervalues[0]))
            print("Initial cluster counts are: ")
            for i in range(self.k):
                print("\t{} => {}".format(i,self.clustercount[i]))
            for i in range(self.numobjects):
                if self.clustership[i] < 0 or self.clustership[i] > self.k:
                    print("Warning: {} is in cluster {}".format(i,self.clustership[i]))
        # END DEBUG CODE
        
        # return total cost and initial clusters built
        return [totaldistance, self.clustervalues]
                    
    def BuildClusters(self):
        # DEBUG CODE
        if self.verbose > 0:
            print("Current cluster counts are: ")
            for i in range(self.k):
                print("\t{} => {}".format(i,self.clustercount[i]))
            for i in range(self.numobjects):
                if self.clustership[i] < 0 or self.clustership[i] > self.k:
                    print("Warning: {} is in cluster {}".format(i,self.clustership[i]))
        # END DEBUG CODE
        
        moves = 0
        for i in range(self.numobjects):
            cluster = self.ClosestCluster(i)[0]
            if self.clustership[i] != cluster:
                moves += 1
                oldcluster = self.clustership[i]
                self.clustership[i] = cluster
                self.clustercount[cluster] += 1
                self.clustercount[oldcluster] -= 1
                for j in range(self.numattributes):
                    if self.data[i][j] in self.clusterfrequency[cluster][j].keys():
                        self.clusterfrequency[cluster][j][self.data[i][j]] += 1
                    else:
                        self.clusterfrequency[cluster][j][self.data[i][j]] = 1
                    self.clusterfrequency[oldcluster][j][self.data[i][j]] -= 1
                    self.clustervalues[cluster][j]      = self.HighestFrequency(cluster,j)
                    self.clustervalues[oldcluster][j]   = self.HighestFrequency(oldcluster,j)
                    
        return [moves,self.numobjects]
                   
    def ClosestCluster(self,o):
        cluster = 0
        mindistance = self.Distance(o,0)
        for i in range(1, self.k):
            distance = self.Distance(o,i)
            if distance < mindistance:
                mindistance = distance
                cluster = i
        return [cluster, mindistance]
    
    def Distance(self,o,c):
        dist = 0
        for i in range(self.numattributes):
            if self.data[o][i] != self.clustervalues[c][i]:
                dist += 1
        return dist
    
    def HighestFrequency(self,c,e):
        mode = ''
        value = 0
        for i in self.clusterfrequency[c][e].keys():
            if self.clusterfrequency[c][e][i] > value:
                mode = i
        return mode
