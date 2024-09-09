<!--
 * @Author: caszhou
 * @Date: 2022-10-21 16:10:08
 * @Description: 权限
-->
<template>
  <div class="app-container" v-loading="loading">
    <div class="app-header">
      <el-page-header @back="goBack" content="权限配置" />
    </div>
    <div class="app-part">
      <div class="app-item">角色名称:</div>
      <div class="app-item">
        {{ roleName }}
      </div>
    </div>
    <div class="app-part">
      <div class="app-item">角色权限:</div>
      <div class="app-item">
        <el-button type="primary" @click="handleExpand(true)">全部展开</el-button>
        <el-button type="primary" @click="handleExpand(false)">全部收起</el-button>
      </div>
    </div>
    <div class="app-main">
      <div class="custom-tree">
        <el-tree
          show-checkbox
          :data="treeData"
          :default-checked-keys="treeDefaultCheckedKeys"
          :props="{ label: 'name', children: 'functionCodeVoList' }"
          node-key="id"
          empty-text="暂无权限"
          ref="treeRef"
        ></el-tree>
      </div>
    </div>
    <div class="app-footer">
      <el-button @click="goBack">取消</el-button>
      <el-button type="primary" @click="handleConfirm">确定</el-button>
    </div>
  </div>
</template>

<script>
import { roleSelectOne, functionCodeAccessList, roleAccessUpdate } from '@/api/sso-idp-server'

export default {
  data() {
    return {
      loading: false,
      pageId: '',
      roleName: '',
      treeData: [],
      treeAllKeys: [],
      treeDefaultCheckedKeys: []
    }
  },
  async mounted() {
    try {
      this.loading = true
      const { id } = this.$route.query
      this.pageId = id
      await Promise.all([this.getAccessTree(), this.getRoleInfo()])
      this.treeAllKeys = this.getAllNodeId(this.treeData)
    } catch (error) {
      console.error('await:', error)
    } finally {
      this.loading = false
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    handleExpand(data) {
      this.treeAllKeys.forEach(item => {
        const functionCodeVoList = this.$refs['treeRef'].store.nodesMap[item].data.functionCodeVoList
        if (functionCodeVoList && functionCodeVoList.length > 0) {
          this.$refs['treeRef'].store.nodesMap[item].expanded = data
        }
      })
    },
    getAllNodeId(treeData) {
      const ids = []
      this.recursiveTreeData(treeData, ids)
      return ids
    },
    recursiveTreeData(tree, ids) {
      tree.forEach(item => {
        ids.push(item.id)
        if (item.functionCodeVoList && item.functionCodeVoList.length > 0) {
          this.recursiveTreeData(item.functionCodeVoList, ids)
        }
      })
    },
    async handleConfirm() {
      try {
        this.loading = true
        // 获取门店选中的node
        const storeCurrentIds = [].concat(
          this.$refs['treeRef'].getCheckedKeys(),
          this.$refs['treeRef'].getHalfCheckedKeys()
        )
        await roleAccessUpdate({ id: this.pageId, functionId: storeCurrentIds })
        this.$notify({
          title: '消息',
          message: '操作成功',
          type: 'success'
        })
        this.$router.go(-1)
      } catch (error) {
        console.error('await:', error)
      } finally {
        this.loading = false
      }
    },
    async getRoleInfo() {
      const { data } = await roleSelectOne({ id: this.pageId })
      this.roleName = data.name
      this.treeDefaultCheckedKeys = data.functionCodeVos?.length ? this.getTreeEnabled(data.functionCodeVos) : []
    },
    async getAccessTree() {
      const { data } = await functionCodeAccessList()
      this.treeData = data
    },
    getTreeEnabled(tree) {
      const result = []
      tree.forEach(item => {
        if (item.functionCodeVoList?.length > 0) {
          const childRes = this.getTreeEnabled(item.functionCodeVoList)
          if (childRes.length > 0) {
            result.push(...childRes)
          }
        } else {
          result.push(item.id)
        }
      })
      return result
    }
  }
}
</script>

<style scoped lang="scss"></style>
