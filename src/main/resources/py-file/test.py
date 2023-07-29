import pandas as pd

if __name__ == "__main__":
    file_path = "csv_file_2519495233696021975.csv"

    sep = "@@@"
    df = pd.read_csv(file_path, sep=sep, encoding='utf-8', engine='python')

    columns = list(df.columns)
    print(columns)

    result_file_path = "result.csv"

    with open(result_file_path, "w", encoding="utf-8") as file:
        columns_result = sep.join(columns)
        file.writelines(columns_result+"\n")
        for index, row in df.iterrows():
            line = ""
            for column in columns:
                line += str(row[column]) + sep
            line = line[:len(line) - len(sep)] + "\n"
            file.writelines(line)
            print(line,end="")

    # print(df)
