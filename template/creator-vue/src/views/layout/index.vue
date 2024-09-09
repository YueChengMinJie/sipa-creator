<template>
  <div id="layout">
    <div class="header-box">
      <unit-header />
    </div>
    <!-- 主体内容 -->
    <div class="container" :class="[{ 'pack-up': isCollapse }, { mobile: isMobile }]">
      <!-- 左侧导航 -->
      <div class="bg-mask" v-show="!isCollapse && isMobile" @click="closeMask"></div>
      <unit-left-menu ref="leftMenu" :collapse.sync="isCollapse" />
      <!-- 左侧导航 结束 -->
      <!-- 右侧主要内容 -->
      <div class="right-main">
        <unit-breadcrumb />
        <div class="right-view">
          <transition name="fade-transform" mode="out-in">
            <keep-alive :include="keepAliveValues"><router-view /></keep-alive>
          </transition>
        </div>
      </div>
      <!-- 右侧主要内容 结束 -->
    </div>
    <!-- 主体内容 结束 -->
  </div>
</template>

<script>
import UnitHeader from './UnitHeader'
import UnitLeftMenu from './UnitLeftMenu'
import UnitBreadcrumb from './UnitBreadcrumb'

export default {
  components: {
    UnitHeader,
    UnitLeftMenu,
    UnitBreadcrumb
  },
  data() {
    return {
      isCollapse: false,
      isMobile: false
    }
  },
  computed: {
    keepAliveValues() {
      return this.$store.state.keepAliveValues
    }
  },
  created() {
    this.handleResize()
  },
  mounted() {
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    handleResize() {
      if (document.body.clientWidth < 1000) {
        if (!this.isMobile) {
          this.isMobile = true
        }
        if (!this.isCollapse) {
          this.isCollapse = true
        }
      } else {
        if (this.isMobile) {
          this.isMobile = false
        }
      }
    },
    closeMask() {
      this.isCollapse = true
    }
  },
  watch: {
    $route() {
      if (this.isMobile && !this.isCollapse) {
        this.isCollapse = true
      }
    }
  }
}
</script>

<style lang="scss" scoped>
$iconWidth: 64px;

#layout {
  width: 100%;
  height: 100%;
  position: relative;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .header-box {
    width: 100%;
    min-width: 980px;
    height: 50px;
    flex-shrink: 0;

    ::v-deep .el-menu-item,
    ::v-deep .el-submenu__title {
      line-height: 48px;
    }
  }
  .container {
    width: 100%;
    height: 100%;
    position: relative;
    flex: 1;
    display: flex;
    overflow: hidden;
  }
  .bg-mask {
    background: #000;
    opacity: 0.3;
    width: 100%;
    height: 100%;
    left: 0;
    bottom: 0;
    position: absolute;
    z-index: 1023;
    transition: 0.3s;
  }
  .right-main {
    width: 100%;
    height: 100%;
    transition: ease-out width 0.3s;
    display: flex;
    flex-direction: column;
    overflow: hidden;
  }
  .pack-up {
    ::v-deep .left-menu {
      width: $iconWidth;
    }
  }
  .mobile {
    overflow: hidden;
    .right-main {
      padding-left: $iconWidth;
    }
    ::v-deep .left-menu {
      position: absolute;
    }
  }
  .right-view {
    flex: 1;
    position: relative;
    padding: 0 15px;
    overflow: auto;

    ::v-deep {
      .app-children {
        width: 100%;
        height: 100%;
        position: relative;
        overflow: auto;
      }
    }
  }
}
</style>
