<!--
 * @Author: caszhou
 * @Date: 2023-06-13 13:02:58
 * @Description: 面包屑
-->
<template>
  <div class="breadcrumb">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item v-for="(item, index) in routes" :key="index + '-' + item.name" :to="{ name: item.name }">
        <template v-if="item.meta.icon">
          <svg-icon :icon-class="item.meta.icon" />
        </template>
        <span class="title">{{ item.meta.title }}</span>
      </el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script>
export default {
  data() {
    return {
      routes: []
    }
  },
  watch: {
    $route: {
      immediate: true,
      handler: function () {
        this.routes = this.$route.matched
      }
    }
  },
  methods: {}
}
</script>

<style lang="scss" scoped>
.breadcrumb {
  padding: 15px 15px 10px 15px;
  color: #333;

  .el-breadcrumb {
    background-color: #fff;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    line-height: 52px;
    padding: 0 15px;
    box-shadow: 0px 2px 3px 0px rgba(227, 227, 227, 0.5);

    ::v-deep .el-breadcrumb__item:last-child .el-breadcrumb__inner {
      color: #707070;
      cursor: text;
    }
    ::v-deep .el-breadcrumb__inner,
    .el-breadcrumb__inner a {
      font-weight: normal;
    }
    .title {
      margin-left: 5px;
    }
  }
}
</style>
