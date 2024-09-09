<!--
 * @Author: caszhou
 * @Date: 2023-07-18 17:18:07
 * @Description: 选择角色
-->
<template>
  <div>
    <el-dialog
      title="选择角色"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="820px"
      @open="handleOpen"
      @closed="handleClosed"
    >
      <div v-loading="loading">
        <div class="app-part">
          <div class="app-item">条件筛选:</div>
          <div class="app-item">
            <el-input
              placeholder="请输入角色名称"
              maxlength="50"
              v-model="search.queryLike"
              @keyup.native.enter="initList"
              clearable
              style="width: 250px"
            >
              <el-button slot="append" icon="el-icon-search" @click="initList"></el-button>
            </el-input>
          </div>
        </div>
        <el-table
          :data="tableData"
          ref="tableRef"
          @selection-change="handleSelectionChange"
          max-height="400"
          row-key="id"
        >
          <el-table-column type="selection" :selectable="filterData" reserve-selection align="center" width="55" />
          <el-table-column label="角色名称" min-width="100" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.name || '--' }}
            </template>
          </el-table-column>
          <el-table-column label="角色描述" min-width="150" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.remark || '--' }}
            </template>
          </el-table-column>
        </el-table>
        <!-- 对应表格分页 -->
        <div class="pagination">
          <el-pagination
            background
            :current-page="pagination.current"
            :page-sizes="pagination.sizes"
            :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pagination.total"
            :pager-count="pagination.pagerCount"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          ></el-pagination>
        </div>
        <!-- 对应表格分页 结束 -->
        <div class="dialog-footer">
          <el-button @click="handleCancel()">取 消</el-button>
          <el-button type="primary" @click="handleConfirm" :disabled="multipleSelection.length === 0">
            确 定
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { roleAccountSelectByCompany } from '@/api/sso-idp-server'
import MixinPage from '@/utils/mixinPage'

export default {
  mixins: [MixinPage],
  props: {
    show: {
      type: Boolean,
      default: false
    },
    currentData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      listApi: roleAccountSelectByCompany,
      dialogVisible: false,
      search: { queryLike: '' }
    }
  },
  watch: {
    show: {
      immediate: true,
      handler: function (val) {
        this.dialogVisible = val
      }
    }
  },
  methods: {
    /** 关闭dialog */
    handleClosed() {
      this.$emit('update:show', false)
      this.$refs.tableRef.clearSelection()
    },
    /** 打开弹窗事件 */
    async handleOpen() {
      try {
        this.loading = true
        await this.getList()
      } catch (error) {
        console.error('await:', error)
      } finally {
        this.loading = false
      }
    },
    /** 关闭弹窗 */
    handleCancel() {
      this.dialogVisible = false
    },
    /** 确认事件 */
    handleConfirm() {
      this.$emit('confirm', this.multipleSelection)
      this.handleCancel()
    },
    filterData(data) {
      return !data.acquiescence && this.currentData.every(item => item.id !== data.id)
    }
  }
}
</script>

<style lang="scss" scoped>
.pagination {
  text-align: center;
  margin-top: 10px;
}
</style>
