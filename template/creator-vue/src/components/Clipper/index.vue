<template>
  <div>
    <el-dialog
      title="图片裁剪"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="890px"
      @open="handleOpen()"
      @close="handleClosed()"
      append-to-body
    >
      <div class="container" v-loading="loading">
        <div class="main">
          <vue-cropper
            class="cropper"
            ref="cropperRef"
            :img="data.url"
            :outputSize="option.outputSize"
            :outputType="option.outputType"
            :info="option.info"
            :canScale="option.canScale"
            :autoCrop="option.autoCrop"
            :autoCropWidth="option.autoCropWidth"
            :autoCropHeight="option.autoCropHeight"
            :fixedNumber="option.fixedNumber"
            :fixed="option.fixed"
            :full="option.full"
            :fixedBox="option.fixedBox"
            :canMove="option.canMove"
            :canMoveBox="option.canMoveBox"
            :centerBox="option.centerBox"
          />
          <div class="info">
            <!-- features -->
            <div class="features">
              <div class="label">图片宽度 :</div>
              <el-input-number
                class="input"
                placeholder="宽"
                v-model="option.fixedNumber[0]"
                :min="1"
                :max="9999"
                :controls="false"
                :precision="1"
                @change="changeInput($event, 'autoCropWidth')"
              />
              <div class="label ml30">图片高度 :</div>
              <el-input-number
                class="input"
                placeholder="高"
                v-model="option.fixedNumber[1]"
                :min="1"
                :max="9999"
                :controls="false"
                :precision="1"
                @change="changeInput($event, 'autoCropHeight')"
              />
              <el-button class="ml30" icon="el-icon-refresh-left" type="primary" @click="rotateLeft()">
                向左旋转90度
              </el-button>
              <el-button icon="el-icon-refresh-right" type="primary" @click="rotateRight()">向右旋转90度</el-button>
            </div>
            <!-- features 结束 -->
            <!-- features -->
            <div class="features">
              <el-checkbox v-model="option.fixed">截图固定比例</el-checkbox>
              <el-checkbox v-model="option.centerBox">截图是否限制在图片里</el-checkbox>
              <el-checkbox v-model="option.canScale">图片缩放</el-checkbox>
              <el-checkbox v-model="option.canMove">图片拖动</el-checkbox>
            </div>
            <!-- features 结束 -->
          </div>
        </div>
        <div class="dialog-footer">
          <el-button @click="handleCancel()">取 消</el-button>
          <el-button type="primary" @click="handleConfirm">确 定</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { VueCropper } from 'vue-cropper'
import Config from '@/config'
import Http from '@/utils/http'

export default {
  components: {
    VueCropper
  },
  props: {
    value: {
      type: Boolean,
      default: false
    },
    data: {
      type: Object,
      required: true
    },
    size: {
      type: Array,
      default: function () {
        return [300, 300]
      }
    }
  },
  data() {
    return {
      dialogVisible: false,
      option: {
        outputSize: 1, // 裁剪生成图片的质量
        outputType: 'png', // 裁剪生成图片的格式
        info: true, // 裁剪框的大小信息
        canScale: false, // 图片是否允许滚轮缩放
        autoCrop: true, // 是否默认生成截图框
        autoCropWidth: '', // 默认生成截图框宽度
        autoCropHeight: '', // 默认生成截图框高度
        fixedNumber: [], // 截图框的宽高比例
        fixed: true, // 是否开启截图框宽高固定比例
        full: true, // 是否输出原图比例的截图
        fixedBox: false, // 不允许改变宽高
        canMove: false, // 上传图片是否可以移动
        canMoveBox: true, // 截图框能否拖动
        centerBox: true // 截图框是否被限制在图片里面
      },
      loading: false,
      isConfirm: false,
      idpHttp: new Http(Config.associationServices[0].url)
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
  methods: {
    // 关闭dialog
    handleClosed() {
      this.$emit('input', false)
      if (!this.isConfirm) {
        this.$emit('cancel', this.data)
      } else {
        this.isConfirm = false
      }
    },
    // 打开时
    handleOpen() {
      this.option.autoCropWidth = this.size[0]
      this.option.autoCropHeight = this.size[1]
      this.option.fixedNumber = [].concat(this.size)
    },
    handleCancel() {
      this.dialogVisible = false
    },
    async handleConfirm() {
      this.loading = true
      this.$refs['cropperRef'].getCropBlob(async blob => {
        try {
          const formData = new FormData()
          formData.append('file', blob)
          const response = await this.idpHttp.httpRequest({
            url: '/os',
            method: 'POST',
            data: formData
          })
          this.isConfirm = true
          this.dialogVisible = false
          this.$emit('confirm', response, this.data)
        } catch (error) {
          console.error('await:', error)
        } finally {
          this.loading = false
        }
      })
    },
    rotateLeft() {
      this.$refs.cropperRef.rotateLeft()
    },
    rotateRight() {
      this.$refs.cropperRef.rotateRight()
    },
    changeInput(e, key) {
      this.option[key] = e
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  height: 60vh;
  display: flex;
  flex-direction: column;
  .main {
    flex: 1;
    overflow: hidden;
    display: flex;
    flex-direction: column;

    .cropper {
      flex: 1;
      overflow: hidden;
    }
  }
  .info {
    flex-shrink: 0;
    padding-top: 10px;

    .features {
      display: flex;
      margin-top: 10px;
      line-height: 32px;

      .label {
        width: 80px;
      }
      .input {
        width: 100px;
      }
      .ml30 {
        margin-left: 30px;
      }
    }
  }
}
</style>
