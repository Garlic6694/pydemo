import pandas as pd

if __name__ == "__main__":
    file_path = "source.csv"

    sep = "@@@"
    df = pd.read_csv(file_path, sep=sep, encoding="utf-8", engine="python")

    print(df.info())

    print(df.head(10))

    source_col = "Id,Age,Pclass"
    source_cols = str(source_col).split(",")

    print(source_cols)

    sub_df = df[source_cols]
    print(sub_df.info())
    print(sub_df.head(10))

