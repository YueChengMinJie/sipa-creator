package com.sipa.tcp.creator.util;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;
import com.sipa.tcp.creator.constant.CreatorConstant;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.extra.servlet.ServletUtil;

/**
 * @author caszhou
 * @date 2022/6/10
 */
public class PathUtil {
    public static String joinPath(String... paths) {
        StringBuilder tmp = new StringBuilder();
        for (String path : paths) {
            if (!Strings.isNullOrEmpty(path)) {
                tmp.append(path);
                tmp.append(File.separator);
            }
        }
        return tmp.deleteCharAt(tmp.lastIndexOf(File.separator)).toString();
    }

    public static String joinPackage(String... packages) {
        StringBuilder tmp = new StringBuilder();
        for (String aPackage : packages) {
            if (!Strings.isNullOrEmpty(aPackage)) {
                tmp.append(aPackage);
                tmp.append(".");
            }
        }
        return tmp.deleteCharAt(tmp.lastIndexOf(".")).toString();
    }

    public static String joinModulePackage(String... packages) {
        if (packages.length > 1) {
            StringBuilder tmp = new StringBuilder();
            String last = null;
            for (int i = 0; i < packages.length; i++) {
                String pkg = packages[i];
                if (i == 0) {
                    int lastPointIdx;
                    if (pkg.endsWith("impl")) {
                        lastPointIdx = (pkg.substring(0, pkg.lastIndexOf("."))).lastIndexOf(".");
                    } else {
                        lastPointIdx = pkg.lastIndexOf(".");
                    }
                    tmp.append(pkg, 0, lastPointIdx);
                    tmp.append(".");
                    last = pkg.substring(lastPointIdx + 1);
                } else {
                    tmp.append(pkg);
                    tmp.append(".");
                }
            }
            tmp.append(last);
            return tmp.toString();
        } else {
            return joinPackage(packages);
        }
    }

    public static String getShortNameFromFullRef(String ref) {
        if (Strings.isNullOrEmpty(ref)) {
            return "";
        }
        if (!ref.contains(".")) {
            return ref;
        }
        return ref.substring(ref.lastIndexOf(".") + 1);
    }

    public static String getDefaultPath() {
        return FileUtil.getUserHomePath() + File.separator + CreatorConstant.CONFIG_HOME;
    }

    public static void downloadZip(HttpServletResponse response, Tuple tuple) {
        // 基础目录
        String baseProjectPath = tuple.get(0);
        // 文件
        File srcFile = new File(baseProjectPath + File.separator + "src");
        // zip
        File zipFile = FileUtil.file(baseProjectPath, FileUtil.mainName(baseProjectPath) + ".zip");
        // 打包
        ZipUtil.zip(srcFile.getAbsolutePath(), zipFile.getAbsolutePath(), true);
        // 下载
        ServletUtil.write(response, FileUtil.getInputStream(zipFile));
        // 删除src和zip
        FileUtil.del(zipFile);
        FileUtil.del(baseProjectPath);
    }
}
