<template>
  <div class="unit-category" :class="{ shrink: shrink }" v-loading="loading">
    <div class="title">组织架构</div>
    <div class="list">
      <el-scrollbar ref="scrollbarRef">
        <el-tree
          ref="treeRef"
          :data="treeData"
          :props="{ label: 'name', children: 'childOrganizations' }"
          node-key="id"
          highlight-current
          :expand-on-click-node="false"
          :default-expanded-keys="defaultExpandedKeys"
          @node-click="handleNodeClick"
        >
          <div slot-scope="{ data, node }" class="intro">
            <span class="name">{{ data.name }}</span>
            <span class="icon" @click.stop>
              <el-tooltip content="新增" transition="el-zoom-in-left">
                <i class="el-icon-circle-plus-outline" @click="handleOperate(node, 'add')"></i>
              </el-tooltip>
              <el-tooltip v-if="node.level !== 1" content="编辑" transition="el-zoom-in-left">
                <i class="el-icon-edit-outline" @click="handleOperate(node, 'edit')"></i>
              </el-tooltip>
              <el-tooltip v-if="node.level !== 1" content="删除" transition="el-zoom-in-left">
                <el-popconfirm
                  title="确认要删除这条数据吗？"
                  placement="top"
                  transition="el-zoom-in-left"
                  @confirm="handleDelete(node)"
                >
                  <i class="el-icon-delete" slot="reference" />
                </el-popconfirm>
              </el-tooltip>
            </span>
          </div>
        </el-tree>
      </el-scrollbar>
    </div>
    <!-- 新增编辑 -->
    <el-dialog
      :title="operateType === 'add' ? '新增' : '编辑'"
      :visible.sync="operateVisible"
      :close-on-click-modal="false"
      width="380px"
      @closed="handleClosed()"
    >
      <div class="dialog-container" v-loading="operateLoading">
        <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px" @submit.native.prevent>
          <el-form-item prop="id" v-show="false"></el-form-item>
          <!-- form-row -->
          <div class="app-row">
            <el-form-item label="上级部门:" prop="parentId">
              <el-cascader
                :options="filterTree(treeData)"
                v-model="formData.parentId"
                :props="{
                  checkStrictly: true,
                  label: 'name',
                  value: 'id',
                  emitPath: false,
                  children: 'childOrganizations'
                }"
                @visible-change="handleCascaderStuck()"
              />
            </el-form-item>
          </div>
          <!-- form-row end -->
          <!-- form-row -->
          <div class="app-row">
            <el-form-item label="部门名称:" prop="name">
              <el-input v-model="formData.name" clearable maxlength="10" placeholder="请输入部门名称"></el-input>
            </el-form-item>
          </div>
          <!-- form-row end -->
          <div class="dialog-footer">
            <el-button @click="handleCancel()">取 消</el-button>
            <el-button type="primary" @click="handleConfirm">确 定</el-button>
          </div>
        </el-form>
      </div>
    </el-dialog>
    <!-- 新增编辑 end -->
  </div>
</template>

<script>
import { getScrollBarWidth, handleCascaderStuck } from '@/utils/commonTools'
import { orgAdd, orgUpdate, orgDelete } from '@/api/sso-idp-server.js'

export default {
  props: {
    shrink: {
      type: Boolean,
      default: false
    },
    treeData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      loading: false,
      defaultExpandedKeys: [],
      operateVisible: false,
      operateType: 'add',
      operateLoading: false,
      formData: {
        parentId: '',
        id: '',
        name: ''
      },
      rules: {
        parentId: [{ required: true, message: '请选择上级部门', trigger: 'blur' }],
        name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }]
      },
      currentData: null
    }
  },
  mounted() {
    const width = getScrollBarWidth()
    this.$refs['scrollbarRef'].$el.children[0].style.height = `calc(100% + ${width}px)`
  },
  methods: {
    handleCascaderStuck,
    handleNodeClick(data) {
      this.currentData = data
      this.$emit('node-click', data)
    },
    handleOperate(node, type) {
      this.operateVisible = true
      setTimeout(() => {
        this.operateType = type
        const data = node.data
        if (this.operateType === 'add') {
          this.formData.id = ''
          this.formData.parentId = data.id
        } else {
          this.formData.parentId = data.parentId
          this.formData.id = data.id
          this.formData.name = data.name
        }
      }, 0)
    },
    handleClosed() {
      this.$refs['formRef'].resetFields()
    },
    async handleConfirm() {
      try {
        this.operateLoading = true
        await this.$refs['formRef'].validate()
        if (this.operateType === 'add') {
          await orgAdd(this.formData)
          this.$notify({
            title: '消息',
            message: '新增成功',
            type: 'success'
          })
        } else {
          await orgUpdate(this.formData)
          this.$notify({
            title: '消息',
            message: '编辑成功',
            type: 'success'
          })
        }
        await this.$parent.initTree()
        this.handleSetCheck()
        this.handleCancel()
      } catch (err) {
        console.error(err)
      } finally {
        this.operateLoading = false
      }
    },
    handleCancel() {
      this.operateVisible = false
    },
    // 删除
    async handleDelete(node) {
      try {
        this.loading = true
        await orgDelete({ ids: [node.data.id] })
        await Promise.all([this.$parent.initTree(), this.$parent.getList()])
        this.$notify({
          title: '消息',
          message: '删除成功',
          type: 'success'
        })
        if (this.currentData.id === node.data.id) {
          this.$parent.tableData = []
          const parentData = node.parent.data
          this.handleSetCheck(parentData.id)
          this.handleNodeClick(parentData)
        } else {
          this.handleSetCheck()
        }
      } catch (err) {
        console.error(err)
      } finally {
        this.loading = false
      }
    },
    // 如果有值，默认展开并选中
    handleSetCheck(id) {
      const key = id || this.currentData?.id
      if (key) {
        this.defaultExpandedKeys = [key]
        this.$refs['treeRef'].setCurrentKey(key)
        this.$nextTick(() => {
          this.currentData = this.$refs['treeRef'].getCurrentNode()
        })
      }
    },
    filterTree(data) {
      const treeData = []
      const recursionFun = (data, treeArray) => {
        for (const item of data) {
          if (item.id !== this.formData.id) {
            const child = []
            if (item.childOrganizations?.length) {
              recursionFun(item.childOrganizations, child)
            }
            treeArray.push({
              id: item.id,
              name: item.name,
              childOrganizations: child.length ? child : undefined
            })
          }
        }
      }
      recursionFun(data, treeData)
      return treeData
    }
  }
}
</script>

<style lang="scss" scoped>
.unit-category {
  width: 280px;
  height: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 5px;
  transition: ease-out 0.3s;
  margin-right: 10px;

  .title {
    background: #409eff;
    border-top-left-radius: 5px;
    border-top-right-radius: 5px;
    width: 280px;
    color: #fff;
    padding: 20px;
  }
  .list {
    width: 280px;
    flex: 1;
    background: #fff;
    position: relative;
    font-size: 13px;

    .el-scrollbar {
      position: absolute;
      left: 0;
      top: 0;
      height: 100%;
      width: 100%;

      ::v-deep .el-scrollbar__view {
        display: flex;
      }
    }
    .el-tree {
      padding: 10px 0;
      flex: 1;
      overflow: hidden;

      ::v-deep.el-tree-node__content {
        height: 30px;
        padding-right: 10px;
      }
    }
    .intro {
      flex: 1;
      overflow: hidden;
      display: flex;
      justify-content: space-between;
      height: 100%;
      align-items: center;

      .name {
        flex: 1;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
    .icon {
      font-size: 14px;
      margin-left: 10px;

      i {
        padding: 3px;
      }
    }
  }
}
.unit-category.shrink {
  width: 0;
  margin-right: 0;
}
.relationship {
  color: #409eff;
  font-size: 13px;

  i {
    color: #606266;
  }
}
</style>
