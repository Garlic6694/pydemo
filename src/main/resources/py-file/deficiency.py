import argparse

import pandas as pd
from sklearn.ensemble import (
    RandomForestClassifier,
    RandomForestRegressor,
    GradientBoostingClassifier,
    GradientBoostingRegressor,
)
from sklearn.linear_model import LogisticRegression, LinearRegression, Ridge, Lasso
from sklearn.naive_bayes import GaussianNB
from sklearn.neighbors import KNeighborsRegressor, KNeighborsClassifier
from sklearn.neural_network import MLPClassifier, MLPRegressor
from sklearn.svm import SVC, SVR
from sklearn.tree import DecisionTreeClassifier, DecisionTreeRegressor


def one_hot_encode_column(df, column_name):
    # 使用get_dummies()函数对指定的column_name列进行哑变量化
    dummies_df = pd.get_dummies(df[column_name], prefix=column_name)

    # 将哑变量化后的DataFrame与原始DataFrame合并
    df = pd.concat([dummies_df, df], axis=1)

    # 删除原始的column_name列
    df.drop(column_name, axis=1, inplace=True)

    return df


def algorithm_switch(k):
    # 分类算法（Classification Algorithms）：标号范围 1-8
    classification_algorithms = {
        1: LogisticRegression(),
        # 逻辑回归（Logistic Regression）
        2: KNeighborsClassifier(),
        # K最近邻算法（K-Nearest Neighbors，KNN）
        3: SVC(kernel="sigmoid"),
        # 支持向量机（Support Vector Machines，SVM）linear 线性 poly 非线性问题 rbf 非线性问题 sigmoid 非线性问题
        4: DecisionTreeClassifier(),
        # 决策树（Decision Trees）
        5: RandomForestClassifier(),
        # 随机森林（Random Forest）
        6: GradientBoostingClassifier(),
        # 梯度提升树（Gradient Boosting）
        7: GaussianNB(),
        # 朴素贝叶斯（Naive Bayes）
        8: MLPClassifier(),
        # 神经网络（Neural Networks）
    }

    # 回归算法（Regression Algorithms）：标号范围 11-18
    regression_algorithms = {
        9: LinearRegression(),
        # 线性回归（Linear Regression）
        10: Ridge(),
        # 岭回归（Ridge Regression）
        11: Lasso(),
        # Lasso回归（Lasso Regression）
        12: SVR(kernel="sigmoid"),
        # 支持向量回归（Support Vector Regression，SVR） linear 线性 poly 非线性问题 rbf 非线性问题 sigmoid 非线性问题
        13: DecisionTreeRegressor(),
        # 决策树回归（Decision Tree Regression）
        14: RandomForestRegressor(),
        # 随机森林回归（Random Forest Regression）
        15: GradientBoostingRegressor(),
        # 梯度提升树回归（Gradient Boosting Regression）
        16: MLPRegressor(max_iter=5000),
        # 神经网络回归（Neural Networks Regression）
    }

    if 1 <= k <= 8:
        return classification_algorithms.get(k, None)
    elif 9 <= k <= 16:
        return regression_algorithms.get(k, None)
    else:
        return None


def set_missing_age(df, source_col, target_col):
    # 把数值类型特征取出来，放入随机森林中进行训练
    tmp_df = df.copy()

    tmp_df.drop("Id", axis=1, inplace=True)
    tmp_df = one_hot_encode_column(tmp_df, "Sex")

    # 乘客分成已知年龄和未知年龄两个部分
    unknown = tmp_df[
        (tmp_df[target_col] == 0)
        | (tmp_df[target_col].isnull())
        | (tmp_df[target_col].isna())
    ]
    known = tmp_df[
        ~(
            (tmp_df[target_col] == 0)
            | (tmp_df[target_col].isnull())
            | (tmp_df[target_col].isna())
        )
    ]

    # 目标数据y
    y = known[target_col]
    # print("y", y)

    # 特征属性数据x
    x = known[known.columns.difference([target_col])]
    # print("x", x)

    # 利用算法进行拟合
    algorithm = algorithm_switch(16)
    algorithm.fit(x, y)

    # 利用训练的模型进行预测
    predicted = algorithm.predict(unknown[unknown.columns.difference([target_col])])

    # 填补缺失的原始数据
    df.loc[
        (
            (tmp_df[target_col] == 0)
            | (tmp_df[target_col].isnull())
            | (tmp_df[target_col].isna())
        ),
        target_col,
    ] = predicted

    return df


if __name__ == "__main__":
    output_str = ""

    parser = argparse.ArgumentParser(description="命令行参数解释器")
    parser.add_argument("--file_path", type=str, default="source.csv", help="源文件")
    parser.add_argument(
        "--result_file_path", type=str, default="result.csv", help="结果文件"
    )
    parser.add_argument("--sep", type=str, default="@@@", help="分隔符")
    parser.add_argument(
        "--source",
        type=str,
        default="Id,Pclass,Sex,SibSp,Parch",
        required=False,
        help="源列，用英文逗号分隔",
    )
    parser.add_argument(
        "--target",
        type=str,
        default="Age",
        required=False,
        help="Description of argument 3",
    )
    args = parser.parse_args()

    file_path = args.file_path
    sep = args.sep
    source_column = str(args.source).split(",")
    target_column = args.target

    try:
        df = pd.read_csv(file_path, sep=sep, encoding="utf-8", engine="python")
        df = set_missing_age(df, source_column, target_column)

        columns = list(df.columns)
        result_file_path = args.result_file_path
        with open(result_file_path, "w", encoding="utf-8") as file:
            columns_result = sep.join(columns)
            file.writelines(columns_result + "\n")
            for index, row in df.iterrows():
                line = ""
                for column in columns:
                    line += str(row[column]) + sep
                line = line[: len(line) - len(sep)] + "\n"
                file.writelines(line)

        output_str = "python exec success"

    except Exception as e:
        output_str = e

    print(output_str)
