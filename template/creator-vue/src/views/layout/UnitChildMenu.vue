<!--
 * @Author: caszhou
 * @Date: 2021-04-20 11:31:02
 * @Description: 导航菜单
-->

<template>
  <div class="child-menu" :class="{ 'level-padding': level === 1 }">
    <template v-for="unit in item">
      <el-submenu v-if="showMenuType(unit) === 'el-submenu'" :index="unit.name" :key="unit.name">
        <template slot="title">
          <svg-icon v-if="unit.meta.icon" :icon-class="unit.meta.icon" class="svg" />
          <span slot="title" class="title">{{ unit.meta.title }}</span>
        </template>
        <unit-child-menu :item="unit.children" />
      </el-submenu>
      <!-- 如果一个没有，并且又有重定向 -->
      <template v-else-if="showMenuType(unit) === 'el-menu-item'">
        <!-- 如果是菜单又没有重定向，说明是空的菜单, 或者children里只有一个时 -->
        <router-link :key="unit.name" :to="{ name: unit.name }" class="custom-link">
          <el-menu-item :index="unit.name">
            <svg-icon v-if="unit.meta.icon" :icon-class="unit.meta.icon" class="svg" />
            <span slot="title" class="title">{{ unit.meta.title }}</span>
          </el-menu-item>
        </router-link>
      </template>
    </template>
  </div>
</template>

<script>
export default {
  name: 'UnitChildMenu',
  props: {
    item: {
      type: Array,
      default: () => {
        return []
      }
    },
    level: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {}
  },
  methods: {
    showMenuType(data) {
      if (data.children && data.children.length > 0) {
        if (this.isChildrenNumberOnly(data)) {
          return 'el-menu-item'
        } else {
          return 'el-submenu'
        }
      } else {
        if (!data.meta.ifMenu || data.redirect !== undefined) {
          return 'el-menu-item'
        } else {
          return 'none'
        }
      }
    },
    /** 判断当前的children从始至终是否只有一个 */
    isChildrenNumberOnly(data) {
      if (!data.children || data.children.length === 0) {
        return true
      } else {
        if (data.children.length === 1) {
          return this.isChildrenNumberOnly(data.children[0])
        } else {
          return false
        }
      }
    }
  }
}
</script>
