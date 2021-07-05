import time 

def fib(n):
    acc = [0, 1]
    for i in range(0, n):
        first = acc.pop(0)
        acc.append(acc[0] + first)
    return acc[0]

num_prewarm = 1000;
num_measurement = 50000;

start = time.time()
for i in range(num_prewarm):
  fib(1000)
end = time.time()
print("prewarm (ms/iteration): " + str((end - start) * 1000 / num_prewarm))

start2 = time.time()
for i in range(num_measurement):
  fib(1000)
end2 = time.time()
print("measurement (ms/iteration): " + str((end2 - start2) * 1000 / num_measurement))
