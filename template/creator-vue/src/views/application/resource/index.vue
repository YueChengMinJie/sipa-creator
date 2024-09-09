<!--
 * @Author: caszhou
 * @Date: 2023-08-08 15:17:36
 * @Description: 资源管理
-->
<template>
  <div class="app-container" v-loading="loading">
    <el-tabs v-model="activeName">
      <el-tab-pane v-for="(item, index) in appList" :key="item.id" :label="item.name" :name="`${index}`"></el-tab-pane>
    </el-tabs>
    <div class="app-part">
      <div class="app-item">功能操作</div>
      <div class="app-item">
        <el-button type="primary" icon="el-icon-plus" @click="handleOperate('add')">新增资源</el-button>
      </div>
    </div>
    <div class="app-table" v-loading="tableLoading">
      <el-table
        ref="tableRef"
        class="drag-table"
        height="100%"
        :data="tableData"
        row-key="id"
        :expand-row-keys="expandRowKeys"
        :tree-props="{ children: 'functionCodeVoList' }"
        @expand-change="handleTableExpandChange"
        v-if="showTable"
      >
        <el-table-column label="资源中文" min-width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.name || '--' }}
          </template>
        </el-table-column>
        <el-table-column label="资源英文" min-width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.aliasName || '--' }}
          </template>
        </el-table-column>
        <el-table-column label="图标" min-width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.icon || '--' }}
          </template>
        </el-table-column>
        <el-table-column label="类型" min-width="100" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ typeOptions.get(scope.row.type) || '--' }}
          </template>
        </el-table-column>
        <el-table-column label="是否隐藏" min-width="100" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-tag v-if="scope.row.hidden" type="info">隐藏</el-tag>
            <el-tag v-else>显示</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="URL" min-width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.url || '--' }}
          </template>
        </el-table-column>
        <el-table-column label="HTML模板路径" min-width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.componentName || '--' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link class="operate" icon="el-icon-edit" @click="handleOperate('edit', scope.row.id)">编辑</el-link>
            <el-popconfirm
              title="你确认进行删除操作？"
              placement="top"
              transition="el-zoom-in-left"
              @confirm="handleTableDelete(scope.row.id, true)"
            >
              <el-link slot="reference" icon="el-icon-delete" class="operate">删除</el-link>
            </el-popconfirm>
            <el-icon class="el-icon-sort operate-sort" />
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import MixinPage from '@/utils/mixinPage'
import { applicationAll, functionCodeList, functionCodeUpdateOrdinal, functionCodeDelete } from '@/api/sso-idp-server'
import Sortable from 'sortablejs'

export default {
  name: 'applicationResourceIndex',
  mixins: [MixinPage],
  data() {
    return {
      listApi: functionCodeList,
      deleteApi: functionCodeDelete,
      deleteKey: 'id',
      activeName: '0',
      appList: [],
      search: { id: '' },
      openWatch: false,
      typeOptions: new Map([
        ['module', '模块'],
        ['menu', '菜单'],
        ['page', '页面'],
        ['endpoint', '按钮']
      ]),
      ifDrag: true,
      tileTableData: [],
      showTable: true,
      expandRowKeys: [],
      tableLoading: false
    }
  },
  async activated() {
    try {
      this.loading = true
      await this.getAppList()
      if (this.appList.length > 0 && !this.search.id) {
        this.search.id = this.appList[0].id
      }
      await this.getTableTree()
      this.$nextTick(() => {
        this.handleDrag()
      })
    } catch (error) {
      console.error('await:', error)
    } finally {
      this.loading = false
      this.openWatch = true
    }
  },
  watch: {
    async activeName() {
      if (this.openWatch) {
        this.search.id = this.appList[this.activeName].id
        this.expandRowKeys = []
        try {
          this.tableLoading = true
          await this.getTableTree()
        } catch (error) {
          console.error('await:', error)
        } finally {
          this.tableLoading = false
        }
      }
    }
  },
  methods: {
    async getAppList() {
      const { data } = await applicationAll()
      this.appList = data
    },
    async getTableTree() {
      if (!this.search.id) {
        return []
      }
      await this.getList()
      this.tileTableData = this.handleTileTable(this.tableData)
    },
    handleDrag() {
      const tbody = document.querySelector('.drag-table tbody')
      Sortable.create(tbody, {
        animation: 300,
        onMove: ({ dragged, related }) => {
          const oldRow = this.tileTableData[dragged.rowIndex]
          const newRow = this.tileTableData[related.rowIndex]
          if (!this.ifDrag || oldRow.pid !== newRow.pid) {
            return false
          }
        },
        onEnd: async ({ oldIndex, newIndex }) => {
          if (oldIndex !== newIndex) {
            try {
              const oldRow = this.tileTableData[oldIndex]
              const index = this.tileTableData.indexOf(oldRow)
              this.ifDrag = false
              const changeIndex = newIndex - oldIndex
              this.tileTableData.splice(index, 1)
              this.tileTableData.splice(index + changeIndex, 0, oldRow)
              const params = this.tileTableData
                .filter(item => item.pid === oldRow.pid)
                .map((item, index) => {
                  return { id: item.id, ordinal: index }
                })
              await functionCodeUpdateOrdinal(params)
              await this.getTableTree()
              this.showTable = false
              this.$nextTick(() => {
                this.showTable = true
                this.$nextTick(() => {
                  this.handleDrag()
                })
              })
            } catch (error) {
              console.error('await:', error)
            } finally {
              this.ifDrag = true
            }
          }
        }
      })
    },
    handleTileTable(tree, result = []) {
      tree.forEach(node => {
        result.push(node)
        if (node.functionCodeVoList && node.functionCodeVoList.length > 0) {
          this.handleTileTable(node.functionCodeVoList, result)
        }
      })
      return result
    },
    handleTableExpandChange(expandedRows, expanded) {
      if (expanded) {
        this.expandRowKeys.push(expandedRows.id)
      } else {
        this.expandRowKeys.splice(this.expandRowKeys.indexOf(expandedRows.id))
      }
    },
    handleOperate(type, id) {
      this.$router.push({
        name: 'applicationResourceOperate',
        query: { type, appId: this.appList[this.activeName].id, id }
      })
    }
  }
}
</script>

<style scoped lang="scss">
.operate-sort {
  margin-left: 10px;
  font-size: 14px;
  vertical-align: middle;
}
</style>
