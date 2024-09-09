<!--
 * @Author: caszhou
 * @Date: 2023-06-15 09:47:54
 * @Description: 上传图片
-->
<template>
  <div class="upload-single-image">
    <el-upload
      class="upload"
      ref="uploadRef"
      :action="action"
      :limit="1"
      :disabled="disabled"
      :fileList="fileList"
      :show-file-list="false"
      :auto-upload="!clip"
      :before-upload="handleBeforeUpload"
      :on-success="handleSuccess"
      :on-error="handleError"
      :on-exceed="handleExceed"
      :on-progress="handleProgress"
      :on-change="handleChange"
      :headers="headers"
    >
      <ul v-if="value || progress !== 100" @click.stop class="el-upload-list el-upload-list--picture-card">
        <li tabindex="0" class="el-upload-list__item is-success">
          <el-progress v-if="progress !== 100" type="circle" :percentage="progress" />
          <el-image
            v-else
            :src="value"
            :preview-src-list="[value]"
            fit="cover"
            ref="viewImgRef"
            class="el-upload-list__item-thumbnail"
          />
          <span class="el-upload-list__item-actions">
            <span class="el-upload-list__item-preview" @click="handlePictureCardPreview()">
              <i class="el-icon-zoom-in"></i>
            </span>
            <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemove()">
              <i class="el-icon-delete"></i>
            </span>
          </span>
        </li>
      </ul>
      <div v-else class="el-upload el-upload--picture-card"><i class="el-icon-plus" /></div>
    </el-upload>
    <!-- 裁剪 -->
    <clipper v-model="clipVisible" :data="clipData" @cancel="handleClipCancel" @confirm="handleClipConfirm" />
  </div>
</template>

<script>
import Config from '@/config'
import Clipper from '@/components/Clipper'

export default {
  components: {
    Clipper
  },
  props: {
    value: {
      type: String,
      default: ''
    },
    /** 文件格式 */
    formats: {
      type: Array,
      default: () => ['jpg', 'jpeg', 'png']
    },
    /** 文件大小 2M */
    size: {
      type: Number,
      default: 2
    },
    /** 是否禁用 */
    disabled: {
      type: Boolean,
      default: false
    },
    /** 是否裁剪 */
    clip: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    value: {
      immediate: true,
      handler: function (newVal) {
        if (newVal) {
          this.fileList = [{ uid: 'custom-1', name: newVal, url: newVal }]
        } else {
          this.fileList.splice(0, 1)
        }
      }
    }
  },
  data() {
    return {
      // ams-service-server
      action: Config.associationServices[0].url + '/os',
      initialIndex: 0,
      headers: {
        Authorization: `Bearer ${this.$store.getters.getToken}`
      },
      progress: 100,
      fileList: [],
      clipVisible: false,
      clipData: {
        uid: '',
        url: ''
      }
    }
  },
  mounted() {},
  methods: {
    handleBeforeUpload(file) {
      const result = this.handleFormatChecks(file)
      if (result) {
        this.progress = 0
      }
      return result
    },
    handleFormatChecks(file) {
      const suffix = file.name.substr(file.name.lastIndexOf('.') + 1)
      if (!this.formats.includes(suffix)) {
        this.$message.error(`上传图片格式只允许为: ${this.formats.join('/')}格式!`)
        return false
      }
      if (file.size / 1024 / 1024 > this.size) {
        this.$message.error(`上传图片大小不能超过: ${this.size}MB!`)
        return false
      }
      return true
    },
    handleSuccess({ data }, file) {
      this.$emit('success', data, file)
      this.handleSetValue(data.url)
    },
    handleError(error) {
      console.error(error)
      this.$notify({
        showClose: true,
        title: '错误',
        message: String(error),
        type: 'error'
      })
    },
    handleExceed() {
      this.$message.error('上传图片不能超过1张')
    },
    handleRemove() {
      this.$emit('remove')
      this.handleSetValue()
    },
    handleSetValue(url = '') {
      this.$emit('input', url)
    },
    handlePictureCardPreview() {
      this.$refs['viewImgRef'].showViewer = true
    },
    handleProgress(event) {
      const num = Math.round(event.percent)
      if (num === 100) {
        this.progress = 99
        setTimeout(() => {
          this.progress = 100
        }, 300)
      } else {
        this.progress = num
      }
    },
    handleChange(file) {
      const checkRes = this.handleFormatChecks(file)
      if (checkRes) {
        if (this.clip && file.status === 'ready') {
          this.clipData.uid = file.uid
          this.clipData.url = URL.createObjectURL(file.raw)
          this.clipVisible = true
        }
      } else {
        const uploadFiles = this.$refs['uploadRef'].uploadFiles
        const findIndex = uploadFiles.findIndex(item => item.uid === file.uid)
        if (findIndex !== -1) {
          uploadFiles.splice(findIndex, 1)
        }
      }
    },
    handleClipConfirm(response, data) {
      const uploadFiles = this.$refs['uploadRef'].uploadFiles
      const findData = uploadFiles.find(item => item.uid === data.uid)
      findData.url = response.data.url
      findData.status = 'success'
      this.handleSuccess(response, findData)
    },
    handleClipCancel(data) {
      const uploadFiles = this.$refs['uploadRef'].uploadFiles
      const findIndex = uploadFiles.findIndex(item => item.uid === data.uid)
      if (findIndex !== -1) {
        uploadFiles.splice(findIndex, 1)
      } else {
        this.$message.error('移除uploadFiles出错')
      }
    }
  }
}
</script>

<style scoped lang="scss">
.upload-single-image {
  overflow: hidden;
  position: relative;

  .upload {
    display: flex;
    flex-wrap: wrap;
  }
}
</style>
