<!--
 * @Author: caszhou
 * @Date: 2023-06-15 09:47:54
 * @Description: 上传图片
-->
<template>
  <div class="upload-images">
    <el-upload
      class="upload"
      :class="{ 'custom-actions': fileList.length >= limit }"
      ref="uploadRef"
      list-type="picture-card"
      :action="action"
      :limit="limit"
      :disabled="disabled"
      :multiple="!clip"
      :auto-upload="!clip"
      :file-list="fileList"
      :before-upload="handleBeforeUpload"
      :on-success="handleSuccess"
      :on-remove="handleRemove"
      :on-error="handleError"
      :on-exceed="handleExceed"
      :on-preview="handlePictureCardPreview"
      :on-change="handleChange"
      :headers="headers"
    >
      <i class="el-icon-plus"></i>
    </el-upload>
    <div class="view-img-content">
      <el-image :src="value[0] || ''" :preview-src-list="value" :initial-index="initialIndex" ref="viewImgRef" />
    </div>
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
      type: Array,
      default: () => []
    },
    /** 文件格式 */
    formats: {
      type: Array,
      default: () => ['jpg', 'jpeg', 'png', 'gif']
    },
    /** 文件大小 2M */
    size: {
      type: Number,
      default: 2
    },
    /** 上传数量 */
    limit: {
      type: Number,
      default: 99 // 最大允许上传数量，默认给99
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
        this.fileList = newVal.map((val, index) => {
          return { uid: `custom-${index + 1}`, name: val, url: val }
        })
      }
    }
  },
  data() {
    return {
      // ams-service-server
      action: Config.associationServices[0].url + '/os',
      fileList: [],
      initialIndex: 0,
      headers: {
        Authorization: `Bearer ${this.$store.getters.getToken}`
      },
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
      return this.handleFormatChecks(file)
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
    handleSuccess({ data }, file, fileList) {
      file.url = data.url
      this.$emit('success', data, file, fileList)
      this.handleSetValue(fileList)
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
      this.$message.error(`上传图片不能超过${this.limit}张`)
    },
    handleRemove(file, fileList) {
      this.$emit('remove', file, fileList)
      this.handleSetValue(fileList)
    },
    handleSetValue(fileList) {
      const values = fileList.map(item => item.url)
      this.$emit('input', values)
    },
    handlePictureCardPreview(file) {
      this.initialIndex = this.value.indexOf(file.url) || 0
      this.$refs['viewImgRef'].showViewer = true
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
      this.handleSuccess(response, findData, uploadFiles)
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
.upload-images {
  overflow: hidden;
  position: relative;

  .upload {
    display: flex;
    flex-wrap: wrap;

    &.custom-actions {
      ::v-deep {
        .el-upload {
          display: none;
        }
      }
    }
  }
  ::v-deep {
    .el-upload {
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
  .view-img-content {
    display: none;
  }
  .custom-list {
    width: 100%;
    height: 100%;
  }
}
</style>
