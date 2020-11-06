import numpy as np
import time


start_time = time.time()

arr = np.arange(15).reshape(3,5)
arr = arr * 3
print(arr)

elapsed_time = time.time() - start_time

print("Elapsed: " + str(elapsed_time * 1000) + "ms")
