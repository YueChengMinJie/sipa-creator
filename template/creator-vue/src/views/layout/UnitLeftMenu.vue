<template>
  <div class="left-menu">
    <el-scrollbar>
      <el-menu :default-active="activeMenu" :unique-opened="true" :collapse="collapse" class="app-menu">
        <unit-child-menu :item="routes" :level="1" />
      </el-menu>
    </el-scrollbar>
    <div class="toggle-sidebar" @click.stop="shrinkMenu">
      <div class="toggle-sidebar-box">
        <i :class="'el-icon-d-arrow-' + (collapse ? 'right' : 'left')"></i>
        <span>收合侧栏</span>
      </div>
    </div>
  </div>
</template>

<script>
import UnitChildMenu from './UnitChildMenu'

export default {
  name: 'LayoutLeftMenu',
  components: {
    UnitChildMenu
  },
  props: {
    collapse: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      specialRoute: []
    }
  },
  computed: {
    activeMenu() {
      const name =
        this.$route.meta && this.$route.meta.highlightName ? this.$route.meta.highlightName : this.$route.name
      return name
    },
    routes() {
      return this.$store.state.routes
    }
  },
  created() {},
  mounted() {},
  methods: {
    shrinkMenu() {
      this.$emit('update:collapse', !this.collapse)
    }
  }
}
</script>

<style lang="scss" scoped>
.left-menu {
  width: 220px;
  height: 100%;
  position: relative;
  z-index: 1024;
  background-color: #fff;
  transition: width 0.3s;
  display: flex;
  flex-direction: column;

  .el-scrollbar {
    flex: 1;

    ::v-deep {
      .el-scrollbar__wrap {
        overflow-x: hidden;
        overflow-y: auto;
      }
    }
    .el-menu {
      border: 0;
      position: relative;
      padding: 18px 0 0 0;

      &:not(.el-menu--collapse) {
        width: 100%;
      }
      a {
        text-decoration: none;
        display: block;
      }

      ::v-deep {
        .svg {
          position: relative;
          font-size: inherit;
          vertical-align: middle;
          margin-right: 5px;
          width: 24px;
          text-align: center;
          color: inherit;

          ::before {
            vertical-align: middle;
          }
        }
        .title {
          display: inline-block;
        }
        .level-padding .child-menu .title {
          padding-left: 8px;
        }
        .is-active > .el-submenu__title {
          color: #409eff;
        }
        .el-menu-item,
        .el-submenu__title {
          line-height: 46px;
          height: 46px;
        }
      }
    }
  }
  .toggle-sidebar {
    flex-shrink: 0;
    line-height: 60px;
    height: 60px;
    border-top: 1px solid #f1f1f1;
    padding: 0 16px 0 22px;
    width: 100%;
    transition: width 0.3s;
    color: #707070;
    font-size: 14px;
    cursor: pointer;
    overflow: hidden;

    i {
      margin-right: 38px;
      font-size: 16px;
      font-weight: bold;
    }
    &:hover {
      background-color: #ecf5ff;
    }

    .toggle-sidebar-box {
      width: 220px;
    }
  }
}
</style>
