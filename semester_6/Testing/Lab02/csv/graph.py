import pandas as pd
import matplotlib.pyplot as plt

log_function = input()
path = f"logs_{log_function}.csv"

df = pd.read_csv(path)
df.plot(x="X", y="Result")
plt.show()