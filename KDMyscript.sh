#!/bin/bash
#PBS -l mem=64gb,nodes=k2n02:ppn=64,walltime=01:40:00
#PBS -m abe
#PBS -o outputs1/20K-gaussian.txt
#PBS -e errs1/20K-gaussian.txt
#!lscpu
//cd COT/
#!javac // you need to give java source file
echo "35-35-30"
echo "35-35-30" >> four_dim.txt
        range=200000
        while [ $range -le 20000000 ]
        do
            n_th=1
            while [ $n_th -le 64 ]
            do
                iter=1
                while [ $iter -le 3 ]
                do
                    if [ $n_th -le 8 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 4 35 70 >> four_dim.txt
                    elif [ $n_th -le 64 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 4 35 70 >> four_dim.txt
                     fi
                ((iter=iter+1))
                done
           ((n_th=n_th*2))
           done
        ((range=range*10))
        done
echo "90-9-1"
echo "90-9-1" >> four_dim.txt
        range=200000
        while [ $range -le 20000000 ]
        do
        	n_th=1
            while [ $n_th -le 64 ]
            do
				iter=1
            	while [ $iter -le 3 ]
            	do
					if [ $n_th -le 8 ]
                  	then
			java LockFreeKDTreeTest $n_th $range 60000 4 90 99 >> four_dim.txt
                    elif [ $n_th -le 64 ]
                    then 
                    java LockFreeKDTreeTest $n_th $range 60000 4 90 99 >> four_dim.txt
                     fi
                ((iter=iter+1))
                done
           ((n_th=n_th*2))
           done
        ((range=range*10))
        done
echo "70-20-10"
echo "70-20-10" >> four_dim.txt
        range=200000
        while [ $range -le 20000000 ]
        do
            n_th=1
            while [ $n_th -le 64 ]
            do
                iter=1
                while [ $iter -le 3 ]
                do
                    if [ $n_th -le 8 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 4 70 90 >> four_dim.txt
                    elif [ $n_th -le 64 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 4 70 90 >> four_dim.txt
                     fi
                ((iter=iter+1))
                done
           ((n_th=n_th*2))
           done
        ((range=range*10))
        done
echo "50-25-25"
echo "50-25-25" >> four_dim.txt
        range=200000
        while [ $range -le 20000000 ]
        do
            n_th=1
            while [ $n_th -le 64 ]
            do
                iter=1
                while [ $iter -le 3 ]
                do
                    if [ $n_th -le 8 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 4 50 75 >> four_dim.txt
                    elif [ $n_th -le 64 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 4 50 75 >> four_dim.txt
                     fi
                ((iter=iter+1))
                done
           ((n_th=n_th*2))
           done
        ((range=range*10))
        done
echo "90-9-1"
echo "90-9-1" >> eight_dim.txt
        range=200000
        while [ $range -le 20000000 ]
        do
        	n_th=1
            while [ $n_th -le 64 ]
            do
				iter=1
            	while [ $iter -le 3 ]
            	do
					if [ $n_th -le 8 ]
                  	then
			java LockFreeKDTreeTest $n_th $range 60000 8 90 99 >> eight_dim.txt
                    elif [ $n_th -le 64 ]
                    then 
                    java LockFreeKDTreeTest $n_th $range 60000 8 90 99 >> eight_dim.txt
                     fi
                ((iter=iter+1))
                done
           ((n_th=n_th*2))
           done
        ((range=range*10))
        done
echo "70-20-10"
echo "70-20-10" >> eight_dim.txt
        range=200000
        while [ $range -le 20000000 ]
        do
            n_th=1
            while [ $n_th -le 64 ]
            do
                iter=1
                while [ $iter -le 3 ]
                do
                    if [ $n_th -le 8 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 8 70 90 >> eight_dim.txt
                    elif [ $n_th -le 64 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 8 70 90 >> eight_dim.txt
                     fi
                ((iter=iter+1))
                done
           ((n_th=n_th*2))
           done
        ((range=range*10))
        done
echo "50-25-25"
echo "50-25-25" >> eight_dim.txt
        range=200000
        while [ $range -le 20000000 ]
        do
            n_th=1
            while [ $n_th -le 64 ]
            do
                iter=1
                while [ $iter -le 3 ]
                do
                    if [ $n_th -le 8 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 8 50 75 >> eight_dim.txt
                    elif [ $n_th -le 64 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 8 50 75 >> eight_dim.txt
                     fi
                ((iter=iter+1))
                done
           ((n_th=n_th*2))
           done
        ((range=range*10))
        done
echo "35-35-30"
echo "35-35-30" >> eight_dim.txt
        range=200000
        while [ $range -le 20000000 ]
        do
            n_th=1
            while [ $n_th -le 64 ]
            do
                iter=1
                while [ $iter -le 3 ]
                do
                    if [ $n_th -le 8 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 8 35 70 >> eight_dim.txt
                    elif [ $n_th -le 64 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 8 35 70 >> eight_dim.txt
                     fi
                ((iter=iter+1))
                done
           ((n_th=n_th*2))
           done
        ((range=range*10))
        done
echo "90-9-1"
echo "90-9-1" >> sixteen_dim.txt
        range=200000
        while [ $range -le 20000000 ]
        do
        	n_th=1
            while [ $n_th -le 64 ]
            do
				iter=1
            	while [ $iter -le 3 ]
            	do
					if [ $n_th -le 8 ]
                  	then
			java LockFreeKDTreeTest $n_th $range 60000 16 90 99 >> sixteen_dim.txt
                    elif [ $n_th -le 64 ]
                    then 
                    java LockFreeKDTreeTest $n_th $range 60000 16 90 99 >> sixteen_dim.txt
                     fi
                ((iter=iter+1))
                done
           ((n_th=n_th*2))
           done
        ((range=range*10))
        done
echo "70-20-10"
echo "70-20-10" >> sixteen_dim.txt
        range=200000
        while [ $range -le 20000000 ]
        do
            n_th=1
            while [ $n_th -le 64 ]
            do
                iter=1
                while [ $iter -le 3 ]
                do
                    if [ $n_th -le 8 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 16 70 90 >> sixteen_dim.txt
                    elif [ $n_th -le 64 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 16 70 90 >> sixteen_dim.txt
                     fi
                ((iter=iter+1))
                done
           ((n_th=n_th*2))
           done
        ((range=range*10))
        done
echo "50-25-25"
echo "50-25-25" >> sixteen_dim.txt
        range=200000
        while [ $range -le 20000000 ]
        do
            n_th=1
            while [ $n_th -le 64 ]
            do
                iter=1
                while [ $iter -le 3 ]
                do
                    if [ $n_th -le 8 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 16 50 75 >> sixteen_dim.txt
                    elif [ $n_th -le 64 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 16 50 75 >> sixteen_dim.txt
                     fi
                ((iter=iter+1))
                done
           ((n_th=n_th*2))
           done
        ((range=range*10))
        done
echo "35-35-30"
echo "35-35-30" >> sixteen_dim.txt
        range=200000
        while [ $range -le 20000000 ]
        do
            n_th=1
            while [ $n_th -le 64 ]
            do
                iter=1
                while [ $iter -le 3 ]
                do
                    if [ $n_th -le 8 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 16 35 70 >> sixteen_dim.txt
                    elif [ $n_th -le 64 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 16 35 70 >> sixteen_dim.txt
                     fi
                ((iter=iter+1))
                done
           ((n_th=n_th*2))
           done
        ((range=range*10))
        done
echo "90-9-1"
echo "90-9-1" >> two_dim.txt
        range=200000
        while [ $range -le 20000000 ]
        do
        	n_th=1
            while [ $n_th -le 64 ]
            do
				iter=1
            	while [ $iter -le 3 ]
            	do
					if [ $n_th -le 8 ]
                  	then
			java LockFreeKDTreeTest $n_th $range 60000 2 90 99 >> two_dim.txt
                    elif [ $n_th -le 64 ]
                    then 
                    java LockFreeKDTreeTest $n_th $range 60000 2 90 99 >> two_dim.txt
                     fi
                ((iter=iter+1))
                done
           ((n_th=n_th*2))
           done
        ((range=range*10))
        done
echo "70-20-10"
echo "70-20-10" >> two_dim.txt
        range=200000
        while [ $range -le 20000000 ]
        do
            n_th=1
            while [ $n_th -le 64 ]
            do
                iter=1
                while [ $iter -le 3 ]
                do
                    if [ $n_th -le 8 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 2 70 90 >> two_dim.txt
                    elif [ $n_th -le 64 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 2 70 90 >> two_dim.txt
                     fi
                ((iter=iter+1))
                done
           ((n_th=n_th*2))
           done
        ((range=range*10))
        done
echo "50-25-25"
echo "50-25-25" >> two_dim.txt
        range=200000
        while [ $range -le 20000000 ]
        do
            n_th=1
            while [ $n_th -le 64 ]
            do
                iter=1
                while [ $iter -le 3 ]
                do
                    if [ $n_th -le 8 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 2 50 75 >> two_dim.txt
                    elif [ $n_th -le 64 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 2 50 75 >> two_dim.txt
                     fi
                ((iter=iter+1))
                done
           ((n_th=n_th*2))
           done
        ((range=range*10))
        done
echo "35-35-30"
echo "35-35-30" >> two_dim.txt
        range=200000
        while [ $range -le 20000000 ]
        do
            n_th=1
            while [ $n_th -le 64 ]
            do
                iter=1
                while [ $iter -le 3 ]
                do
                    if [ $n_th -le 8 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 2 35 70 >> two_dim.txt
                    elif [ $n_th -le 64 ]
                    then
                    java LockFreeKDTreeTest $n_th $range 60000 2 35 70 >> two_dim.txt
                     fi
                ((iter=iter+1))
                done
           ((n_th=n_th*2))
           done
        ((range=range*10))
        done


