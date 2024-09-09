/*
 * @Author: caszhou
 * @Date: 2022-07-01 13:31:19
 * @Description: eslint配置文件
 */

module.exports = {
  root: true,
  env: {
    node: true
  },
  extends: ['plugin:vue/essential', 'eslint:recommended', 'plugin:prettier/recommended'],
  parserOptions: {
    parser: '@babel/eslint-parser'
  },
  rules: {
    'vue/multi-word-component-names': 'off',
    'vue/no-mutating-props': 'off',
    'no-console': 'off',
    'no-async-promise-executor': 0
  }
}
