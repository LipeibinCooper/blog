#!/bin/bash

# 替换数据库中的原作者信息
echo "正在替换数据库中的原作者信息..."
mysql -u root -p88888888 weblog -e "UPDATE t_blog_settings SET github_homepage='', csdn_homepage='', gitee_homepage='', zhihu_homepage='', mail='admin@example.com' WHERE id=1;"

# 替换Java文件中的作者信息
echo "正在替换Java文件中的作者信息..."
find ./weblog-springboot -type f -name "*.java" -exec sed -i 's/@author: 木萨·塔布提/@author: Admin/g' {} \;
find ./weblog-springboot -type f -name "*.java" -exec sed -i 's/@Author: 木萨·塔布提/@Author: Admin/g' {} \;
find ./weblog-springboot -type f -name "*.java" -exec sed -i 's/@url: blog.arnasoft.site/@url: example.com/g' {} \;

# 替换配置文件中的作者信息
echo "正在替换配置文件中的作者信息..."
find ./weblog-springboot -type f -name "*.yml" -exec sed -i 's/com.quanxiaoha/com.example/g' {} \;

# 替换前端文件中的作者信息
echo "正在替换前端文件中的作者信息..."
find ./weblog-vue3 -type f -name "*.vue" -exec sed -i 's/href="https:\/\/www.quanxiaoha.com\//href="https:\/\/example.com\//g' {} \;
find ./weblog-vue3 -type f -name "*.vue" -exec sed -i 's/src="https:\/\/img.quanxiaoha.com/src="https:\/\/example.com/g' {} \;
find ./weblog-vue3 -type f -name "*.vue" -exec sed -i 's/href="https:\/\/qarabala.gitee.io\/blog\//href="https:\/\/example.com\//g' {} \;

# 替换README文件中的作者信息
echo "正在替换README文件中的作者信息..."
sed -i 's/blog.arnasoft.site/example.com/g' README.md
sed -i 's/犬小哈老师/原作者/g' README.md
sed -i 's/www.quanxiaoha.com/example.com/g' README.md

echo "替换完成！" 