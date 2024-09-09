<!--
 * @Author: caszhou
 * @Date: 2021-04-28 17:22:57
 * @Description: 修改头像dialog
-->

<template>
  <div>
    <el-dialog
      title="修改头像"
      :visible.sync="dialogVisible"
      width="480px"
      :close-on-click-modal="false"
      @open="handleOpen"
      @closed="handleClosed"
    >
      <div v-loading="loading">
        <el-tabs v-model="formData.activeName" stretch>
          <el-tab-pane label="系统头像" name="first">
            <p>可使用系统头像选择一张图片作为头像</p>
            <div class="system">
              <div class="item" @click="setAvatar(1)">
                <img :src="require('@/assets/default-avatar1.jpg')" />
                <i v-if="formData.type == 1" class="el-icon-circle-check" />
              </div>
              <div class="item" @click="setAvatar(2)">
                <img :src="require('@/assets/default-avatar2.jpg')" />
                <i v-if="formData.type == 2" class="el-icon-circle-check" />
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="本地头像" name="second">
            <p style="text-align: center">从电脑里挑选一张好图作为头像</p>
            <div class="avatar-box">
              <upload-single-image :clip="isCrop" v-model="formData.src" :formats="['jpg', 'jpeg', 'png', 'gif']" />
            </div>
            <div class="crop">
              <el-checkbox v-model="isCrop">上传前是否裁剪</el-checkbox>
            </div>
            <p style="text-align: center">支持jpg/gif/png格式图片,文件需小于2M</p>
          </el-tab-pane>
        </el-tabs>
        <div class="dialog-footer">
          <el-button @click="handleCancel()">取 消</el-button>
          <el-button type="primary" @click="handleConfirm">确 定</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { accountOnlyUpdate } from '@/api/sso-idp-server'
import UploadSingleImage from '@/components/UploadSingleImage'

export default {
  components: { UploadSingleImage },
  props: {
    value: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      loading: false,
      isCrop: true,
      formData: { type: 1, src: '', activeName: 'first' }
    }
  },
  watch: {
    value: {
      immediate: true,
      handler: function (val) {
        this.dialogVisible = val
      }
    }
  },
  computed: {
    userInfo() {
      return this.$store.state.userInfo
    }
  },
  methods: {
    handleOpen() {
      if (this.userInfo.headImg) {
        switch (this.userInfo.headImg) {
          case '1':
            this.formData.type = 1
            break
          case '2':
            this.formData.type = 2
            break
          default:
            this.formData.src = this.userInfo.headImg
            this.formData.activeName = 'second'
        }
      } else {
        this.formData.type = 1
      }
    },
    // 关闭dialog
    handleClosed() {
      this.formData.type = 1
      this.formData.src = ''
      this.formData.activeName = 'first'
      this.$emit('input', false)
    },
    handleCancel() {
      this.dialogVisible = false
    },
    async handleConfirm() {
      try {
        this.loading = true
        let avatarUrl = ''
        if (this.formData.activeName === 'second') {
          if (!this.formData.src) {
            this.$message.warning('上传头像不能为空!')
            return false
          }
          avatarUrl = this.formData.src
        } else {
          avatarUrl = this.formData.type
        }
        await accountOnlyUpdate({ id: this.userInfo.id, headImg: avatarUrl })
        await this.$store.dispatch('updateUserInfo')
        this.handleCancel()
        this.$notify({
          title: '消息',
          message: '操作成功',
          type: 'success'
        })
      } catch (error) {
        console.error('await:', error)
      } finally {
        this.loading = false
      }
    },
    setAvatar(val) {
      this.formData.type = val
    }
  }
}
</script>

<style lang="scss" scoped>
.system {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;

  .item {
    position: relative;
    margin: 20px;
  }
  img {
    display: block;
    width: 120px;
    height: 120px;
    border-radius: 50%;
  }
  i {
    position: absolute;
    right: 0;
    top: 10px;
    background-color: #fff;
    border-radius: 50%;
    color: #259b24;
    margin-right: 4px;
    font-size: 26px;
  }
}
.avatar-uploader {
  text-align: center;
  width: 100px;
  margin: 0 auto;

  .icon {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 100%;
    height: 100px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 28px;
  }
  img {
    width: 100px;
    height: 100px;
    display: block;
    border-radius: 5px;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
  }
}
.crop {
  display: flex;
  justify-content: center;
  padding-top: 15px;
}
.avatar-box {
  display: flex;
  justify-content: center;
}
</style>
