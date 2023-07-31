import argparse

import pandas as pd
from scipy import stats
from sklearn.cluster import DBSCAN
from sklearn.ensemble import IsolationForest

from sklearn.neighbors import LocalOutlierFactor
from sklearn.preprocessing import StandardScaler

from sklearn.svm import OneClassSVM


def detect_outliers_dbscan(df, column_name, epsilon=0.5, min_samples=5):
    # 获取要进行聚类的特征列数据
    X = df[column_name].values.reshape(-1, 1)

    # 数据标准化
    scaler = StandardScaler()
    X_scaled = scaler.fit_transform(X)

    # 创建DBSCAN模型
    dbscan = DBSCAN(eps=epsilon, min_samples=min_samples)

    # 进行聚类
    df["Cluster"] = dbscan.fit_predict(X_scaled)

    # 噪声点的Cluster标记为-1
    df["IsOutlier"] = df["Cluster"] == -1

    return df[df["IsOutlier"]]


def detect_outliers_oneclass_svm(df, column_name, contamination=0.05):
    # 创建One-Class SVM模型
    ocsvm = OneClassSVM(nu=contamination)

    # 将数据转换为二维数组形式，One-Class SVM要求输入为二维数组
    X = df[column_name].values.reshape(-1, 1)

    # 使用One-Class SVM进行训练和预测
    preds = ocsvm.fit_predict(X)

    # 预测结果中，-1标记表示异常值
    df["IsOutlier"] = preds

    # 返回异常值的DataFrame
    return df[df["IsOutlier"] == -1]


def detect_outliers_lof(df, column_name, contamination=0.05):
    # 创建Local Outlier Factor模型
    lof = LocalOutlierFactor(contamination=contamination)

    # 将数据转换为二维数组形式，Local Outlier Factor要求输入为二维数组
    X = df[column_name].values.reshape(-1, 1)

    # 使用Local Outlier Factor进行训练和预测
    preds = lof.fit_predict(X)

    # 预测结果中，-1标记表示异常值
    df["IsOutlier"] = preds

    # 返回异常值的DataFrame
    return df[df["IsOutlier"] == -1]


def detect_outliers_isolation_forest(df, column_name, contamination=0.05):
    # 创建Isolation Forest模型
    iso_forest = IsolationForest(contamination=contamination, random_state=42)

    # 将数据转换为二维数组形式，Isolation Forest要求输入为二维数组
    X = df[column_name].values.reshape(-1, 1)

    # 使用Isolation Forest进行训练
    iso_forest.fit(X)

    # 预测异常值
    df["IsOutlier"] = iso_forest.predict(X)

    # 返回异常值的DataFrame
    return df[df["IsOutlier"] == -1]


def detect_outliers_zscore(df, column_name, threshold=3):
    # 获取要进行异常值检测的特征列数据
    X = df[column_name].values.reshape(-1, 1)

    # 数据标准化
    scaler = StandardScaler()
    X_scaled = scaler.fit_transform(X)

    # 计算Z-score
    z_scores = stats.zscore(X_scaled)

    # 获取Z-score绝对值大于阈值的数据点索引
    outlier_indices = abs(z_scores) > threshold

    # 返回异常值的DataFrame
    return df[outlier_indices]


# Function to switch between outlier detection methods
def detect_outliers(df, column_name, method, **kwargs):
    if method == 1:
        return detect_outliers_zscore(df, column_name, **kwargs)
    elif method == 2:
        return detect_outliers_isolation_forest(df, column_name, **kwargs)
    elif method == 3:
        return detect_outliers_lof(df, column_name, **kwargs)
    elif method == 4:
        return detect_outliers_oneclass_svm(df, column_name, **kwargs)
    elif method == 5:
        return detect_outliers_dbscan(df, column_name, **kwargs)
    else:
        raise ValueError("Invalid method. Please choose a valid outlier detection method.")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="命令行参数解释器")
    parser.add_argument("--file_path", type=str, default="source.csv", help="源文件")
    parser.add_argument(
        "--result_file_path", type=str, default="result.csv", help="结果文件"
    )
    parser.add_argument("--sep", type=str, default="@@@", help="分隔符")
    parser.add_argument(
        "--target",
        type=str,
        default="Age",
        required=False,
        help="Description of argument 3",
    )

    parser.add_argument(
        "--alg",
        type=str,
        default=2,
        required=False,
        help="1:zscore, 2:isolation_forest, 3:lof, 4:oneclass_svm, 5:dbscan",
    )
    args = parser.parse_args()

    file_path = args.file_path
    sep = args.sep
    target_col = args.target

    df = pd.read_csv(file_path, sep=sep, encoding="utf-8", engine="python")
    # 判断target列中的异常值
    method = args.alg
    kwargs = {}  # Additional arguments for the selected method
    outliers = detect_outliers(df, target_col, method=method, **kwargs)

    # 输出异常值
    print("异常值：", outliers["Id"].values)
    print(outliers)

    """
     Z-score方法 ：使用数据的均值和标准差来计算Z-score，Z-score超过设定阈值的数据点被认为是异常值。

     Isolation Forest（孤立森林）：孤立森林是一种基于树的异常值检测方法，它利用树结构将异常值隔离到较短的路径上，从而更容易检测出异常值。

     Local Outlier Factor（局部离群因子）：局部离群因子是一种基于密度的异常值检测方法，它通过计算每个数据点与其最近邻之间的密度差异来判断异常值。

     One-Class SVM（单类支持向量机）：单类SVM是一种监督学习算法，它尝试将所有数据点映射到一个高维空间，使得正常数据点位于较小的球体内，而异常值位于较大的球体外。

     DBSCAN（基于密度的聚类算法）：DBSCAN是一种聚类算法，它可以将数据点分为核心点、边界点和噪声点。噪声点通常被认为是异常值。

    """
