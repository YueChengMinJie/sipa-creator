<!--
 * @Author: caszhou
 * @Date: 2023-06-13 13:02:58
 * @Description: 文件用途描述
-->
<template>
  <div class="unit-header">
    <!-- 左侧logo + 菜单 -->
    <div class="app-left">
      <div class="svg-logo">
        <el-image :src="require('@/assets/logo.jpeg')" class="logo" />
      </div>
    </div>
    <!-- 右侧个人头像 -->
    <div class="app-right">
      <unit-user-info />
    </div>
    <!-- 右侧个人头像 结束 -->
  </div>
</template>

<script>
import UnitUserInfo from './UnitUserInfo'

export default {
  data() {
    return {}
  },
  components: {
    UnitUserInfo
  },
  mounted() {},
  methods: {}
}
</script>

<style lang="scss" scoped>
.unit-header {
  width: 100%;
  height: 100%;
  min-width: 750px;
  display: flex;
  justify-content: space-between;
  background-color: #fff;
  box-shadow: 0px 2px 3px 0px rgba(227, 227, 227, 0.5);

  .app-left {
    display: flex;
    flex: 1;

    .svg-logo {
      height: 100%;
      display: flex;
      align-items: center;
      padding-left: 14px;
      width: 220px;

      .logo {
        width: 180px;
        height: 40px;
      }
    }
    .el-menu.el-menu--horizontal {
      border: 0;
    }
    .svg {
      margin-right: 4px;
      color: inherit;
    }
  }
  .app-right {
    display: flex;
    padding: 0 15px 0 5px;
  }
}
</style>
