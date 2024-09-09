/*
 * @Author: caszhou
 * @Date: 2022-03-28 10:55:59
 * @Description: Postcss 配置文件
 */

module.exports = {
  plugins: {
    autoprefixer: {
      overrideBrowserslist: ['> 1%', 'last 3 versions']
    }
  }
}
