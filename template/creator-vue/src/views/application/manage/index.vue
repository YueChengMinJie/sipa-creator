<!--
 * @Author: caszhou
 * @Date: 2023-08-08 15:06:37
 * @Description: 应用管理
-->
<template>
  <div class="app-container" v-loading="loading">
    <div class="app-part">
      <div class="app-item">功能操作:</div>
      <div class="app-item">
        <el-button type="primary" icon="el-icon-plus" @click="handleOperate('add')">新增应用</el-button>
      </div>
    </div>
    <!-- 公共表格 -->
    <div class="app-table">
      <el-table height="100%" :data="tableData">
        <el-table-column label="应用名称" min-width="100" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.name || '--' }}
          </template>
        </el-table-column>
        <el-table-column label="URL" min-width="100" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.url || '--' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="120">
          <template slot-scope="scope">
            <el-link icon="el-icon-edit" class="operate" @click="handleOperate('edit', scope.row.id)">编辑</el-link>
            <el-popconfirm
              title="你确认进行删除操作？"
              placement="top"
              transition="el-zoom-in-left"
              @confirm="handleTableDelete(String(scope.row.id), true)"
            >
              <el-link slot="reference" icon="el-icon-delete" class="operate">删除</el-link>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 公共表格 结束 -->
  </div>
</template>

<script>
import MixinPage from '@/utils/mixinPage'
import { applicationAll, applicationDelete } from '@/api/sso-idp-server'

export default {
  name: 'applicationManageIndex',
  mixins: [MixinPage],
  data() {
    return {
      listApi: applicationAll,
      deleteApi: applicationDelete,
      deleteKey: 'id'
    }
  },
  async activated() {
    try {
      this.loading = true
      await this.getList()
    } catch (error) {
      console.error('await:', error)
    } finally {
      this.loading = false
    }
  },
  methods: {
    handleOperate(type, id) {
      this.$router.push({ name: 'applicationManageOperate', query: { type, id } })
    }
  }
}
</script>

<style scoped lang="scss"></style>
