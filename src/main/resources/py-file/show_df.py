import pandas as pd

if __name__ == "__main__":
    file_path = "result.csv"

    sep = "@@@"
    df = pd.read_csv(file_path, sep=sep, encoding="utf-8", engine="python")

    print(df.info())

    print(df.head(10))

