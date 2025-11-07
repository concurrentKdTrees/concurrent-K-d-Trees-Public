## Running the Concurrent k-d Tree

### Compilation
```bash
javac LockFreeKDTreeTest.java

### Execution

```bash
java LockFreeKDTreeTest 8 200000 60000 4 35 70
```

### Parameter Description

| Parameter                     | Meaning                                                                                           |
| ----------------------------- | ------------------------------------------------------------------------------------------------- |
| 8                         | Number of concurrent threads                                                                      |
| 200000                    | Range (domain) of input values                                                                    |
| 60000                     | Duration of execution in milliseconds (≈60 seconds)                                               |
| 4                         | Number of dimensions                                                                              |
| 35                        | Percentage of `contains()` operations                                                             |
| 70                        | Cumulative percentage for `contains()` + `insert()` operations (i.e., `insert()` = 70 − 35 = 35%) |
| Remaining (100 − 70 = 30) | Percentage of `delete()` operations                                                               |

### Example

The above command executes the benchmark with:

 8 threads
 200 K input range
 4-dimensional points
 60 s runtime
 35 % contains, 35 % insert, and 30 % delete operations


