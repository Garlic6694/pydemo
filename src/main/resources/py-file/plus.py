import argparse


def main(args):
    print("Script name:", args.script_name)
    print("Argument 1:", args.arg1)
    print("Argument 2:", args.arg2)
    print("Argument 3:", args.arg3)


import pandas as pd

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Description of your script")
    parser.add_argument("--file_path", type=str, default="", help="源文件")
    parser.add_argument("--result_file_path", type=str, default="", help="结果文件")
    parser.add_argument("--sep", type=str, default="", help="分隔符")
    parser.add_argument("--arg2", type=float, default=0.1, required=False, help="Description of argument 2")
    parser.add_argument("--arg3", type=int, default=1, required=False, help="Description of argument 3")
    args = parser.parse_args()

    file_path = args.file_path
    sep = args.sep

    try:
        df = pd.read_csv(file_path, sep=sep, encoding='utf-8', engine='python')
        columns = list(df.columns)

        result_file_path = args.result_file_path

        with open(result_file_path, "w", encoding="utf-8") as file:
            columns_result = sep.join(columns)
            file.writelines(columns_result + "\n")
            for index, row in df.iterrows():
                line = ""
                for column in columns:
                    line += str(row[column]) + sep
                line = line[:len(line) - len(sep)] + "\n"
                file.writelines(line)
    except Exception as e:
        with open("error.txt", "w", encoding="utf-8") as file:
            file.writelines(str(e))
        print("python exec failed")

    print("python exec success")
