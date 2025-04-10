<template>
  <div>
    <!-- 表头分页查询条件， shadow="never" 指定 card 卡片组件没有阴影 -->
    <el-card shadow="hover" class="mb-5 search-card rounded-xl">
      <!-- flex 布局，内容垂直居中 -->
      <div class="flex items-center flex-wrap gap-4">
        <el-text class="text-gray-700 font-medium">灵感名称</el-text>
        <div class="w-52 mr-5">
          <el-input
            v-model="searchCategoryName"
            placeholder="请输入（模糊查询）"
            class="rounded-lg search-input"
          />
        </div>

        <el-text class="text-gray-700 font-medium">创建日期</el-text>
        <div class="w-auto mr-5">
          <!-- 日期选择组件（区间选择） -->
          <el-date-picker
            v-model="pickDate"
            type="daterange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            size="default"
            :shortcuts="shortcuts"
            @change="datepickerChange"
            class="date-picker rounded-lg"
          />
        </div>

        <el-button
          type="primary"
          class="search-btn rounded-lg transition-all duration-300 hover:shadow-md"
          :icon="Search"
          @click="getTableData"
          >查询</el-button
        >
        <el-button 
          class="reset-btn rounded-lg transition-all duration-300 hover:shadow-md" 
          :icon="RefreshRight" 
          @click="reset"
          >重置</el-button
        >
      </div>
    </el-card>

    <el-card shadow="hover" class="main-card rounded-xl">
      <!-- 新增按钮 -->
      <div class="mb-6">
        <el-button 
          type="primary" 
          class="create-btn rounded-lg transition-all duration-300 hover:shadow-md hover:translate-y-[-2px]"
          @click="addCategoryBtnClick"
        >
          <el-icon class="mr-1">
            <Plus />
          </el-icon>
          新增灵感
        </el-button>
      </div>

      <!-- 分页列表 -->
      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="tableLoading"
        class="custom-table"
        :header-cell-style="{
          background: '#f9fafb',
          color: '#374151',
          fontWeight: '600',
          fontSize: '14px',
          height: '56px'
        }"
        :cell-style="{
          fontSize: '14px',
          padding: '16px 0',
          color: '#4b5563'
        }"
        :row-style="{
          transition: 'all 0.3s',
          borderBottom: '1px solid #f3f4f6'
        }"
        :row-class-name="tableRowClassName"
      >
        <el-table-column prop="name" label="灵感名称" min-width="180">
          <template #default="scope">
            <div class="title-cell">
              <span class="title-text font-medium">{{ scope.row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="articlesTotal" label="灵感笔记数" width="120" align="center">
          <template #default="scope">
            <div class="flex items-center justify-center">
              <el-icon class="mr-1 text-purple-500"><Document /></el-icon>
              <span>{{ scope.row.articlesTotal || 0 }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="readNum" label="浏览量" width="120" align="center">
          <template #default="scope">
            <div class="flex items-center justify-center">
              <el-icon class="mr-1 text-blue-500"><View /></el-icon>
              <span>{{ scope.row.readNum || 0 }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="commentCount" label="评论量" width="120" align="center">
          <template #default="scope">
            <div class="flex items-center justify-center">
              <el-icon class="mr-1 text-green-500"><ChatDotRound /></el-icon>
              <span>{{ scope.row.commentCount || 0 }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" align="center" />
        <el-table-column label="操作" align="center" width="120">
          <template #default="scope">
            <div class="action-buttons flex justify-center">
              <el-button
                type="danger"
                size="small"
                class="delete-btn rounded-lg transition-all duration-300 hover:shadow-sm"
                @click="deleteCategorySubmit(scope.row)"
              >
                <el-icon class="mr-1">
                  <Delete />
                </el-icon>
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="mt-10 flex justify-center">
        <el-pagination
          v-model:current-page="current"
          v-model:page-size="size"
          :page-sizes="[10, 20, 50]"
          :small="false"
          :background="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="getTableData"
          class="custom-pagination"
        />
      </div>
    </el-card>

    <!-- 添加灵感 -->
    <FormDialog
      ref="formDialogRef"
      title="添加灵感笔记灵感"
      destroyOnClose
      @submit="onSubmit"
    >
      <el-form ref="formRef" :rules="rules" :model="form">
        <el-form-item
          label="灵感名称"
          prop="name"
          label-width="80px"
          size="large"
        >
          <el-input
            v-model="form.name"
            placeholder="请输入灵感名称"
            maxlength="20"
            show-word-limit
            clearable
          />
        </el-form-item>
      </el-form>
    </FormDialog>
  </div>
</template>

<script setup>
import { Search, RefreshRight, Plus, Delete, View, ChatDotRound, Document } from '@element-plus/icons-vue'
import { ref, reactive } from 'vue'
import {
  getCategoryPageList,
  addCategory,
  deleteCategory
} from '@/api/admin/category'
import moment from 'moment'
import { showMessage, showModel } from '@/composables/util'
import FormDialog from '@/components/FormDialog.vue'

// 分页查询的灵感名称
const searchCategoryName = ref('')
// 日期
const pickDate = ref('')

// 查询条件：开始结束时间
const startDate = reactive({})
const endDate = reactive({})

// 监听日期组件改变事件，并将开始结束时间设置到变量中
const datepickerChange = e => {
  startDate.value = moment(e[0]).format('YYYY-MM-DD')
  endDate.value = moment(e[1]).format('YYYY-MM-DD')

  console.log('开始时间：' + startDate.value + ', 结束时间：' + endDate.value)
}

const shortcuts = [
  {
    text: '最近一周',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    }
  },
  {
    text: '最近一个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    }
  },
  {
    text: '最近三个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      return [start, end]
    }
  }
]

// 表格加载 Loading
const tableLoading = ref(false)
// 表格数据
const tableData = ref([])
// 当前页码，给了一个默认值 1
const current = ref(1)
// 总数据量，给了个默认值 0
const total = ref(0)
// 每页显示的数据量，给了个默认值 10
const size = ref(10)

// 获取分页数据
function getTableData () {
  // 显示表格 loading
  tableLoading.value = true
  // 调用后台分页接口，并传入所需参数

  getCategoryPageList({
    current: current.value,
    size: size.value,
    startDate: startDate.value,
    endDate: endDate.value,
    name: searchCategoryName.value
  })
    .then(res => {
      if (res.success == true) {
        tableData.value = res.data
        current.value = res.current
        size.value = res.size
        total.value = res.total
      }
    })
    .finally(() => (tableLoading.value = false)) // 隐藏表格 loading
}
getTableData()

// 每页展示数量变更事件
const handleSizeChange = chooseSize => {
  console.log('选择的页码' + chooseSize)
  size.value = chooseSize
  getTableData()
}

// 重置查询条件
const reset = () => {
  searchCategoryName.value = ''
  pickDate.value = ''
  startDate.value = null
  endDate.value = null
}

// 对话框是否显示
const formDialogRef = ref(null)

// 新增灵感按钮点击事件
const addCategoryBtnClick = () => {
  formDialogRef.value.open()
}

// 表单引用
const formRef = ref(null)

// 添加灵感笔记灵感表单对象
const form = reactive({
  name: ''
})

// 规则校验
const rules = {
  name: [
    {
      required: true,
      message: '灵感名称不能为空',
      trigger: 'blur'
    },
    {
      min: 1,
      max: 20,
      message: '灵感名称字数要求大于 1 个字符，小于 20 个字符',
      trigger: 'blur'
    }
  ]
}

const onSubmit = () => {
  // 先验证 form 表单字段
  formRef.value.validate(valid => {
    if (!valid) {
      console.log('表单验证不通过')
      return false
    }

    // 显示提交按钮 loading
    formDialogRef.value.showBtnLoading()
    addCategory(form)
      .then(res => {
        if (res.success == true) {
          showMessage('添加成功')
          // 将表单中灵感名称置空
          form.name = ''
          // 隐藏对话框
          formDialogRef.value.close()
          // 重新请求分页接口，渲染数据
          getTableData()
        } else {
          // 获取服务端返回的错误消息
          let message = res.message
          // 提示错误消息
          showMessage(message, 'error')
        }
      })
      .finally(() => formDialogRef.value.closeBtnLoading()) // 隐藏提交按钮 loading
  })
}

// 删除灵感
const deleteCategorySubmit = row => {
  console.log(row)
  showModel('是否确定要删除该灵感？')
    .then(() => {
      deleteCategory(row.id).then(res => {
        if (res.success == true) {
          showMessage('删除成功')
          // 重新请求分页接口，渲染数据
          getTableData()
        } else {
          // 获取服务端返回的错误消息
          let message = res.message
          // 提示错误消息
          showMessage(message, 'error')
        }
      })
    })
    .catch(() => {
      console.log('取消了')
    })
}

// 表格行样式
const tableRowClassName = ({ row, rowIndex }) => {
  return 'table-row hover:bg-gray-50'
}
</script>

<style scoped>
.custom-table {
  border-radius: 0.5rem;
  overflow: hidden;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06);
}

.search-card, .main-card {
  transition: all 0.3s;
}

.search-btn, .reset-btn, .create-btn, .delete-btn {
  transition: all 0.3s;
}

.custom-pagination :deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background-color: #409eff;
}

.title-cell {
  display: flex;
  align-items: center;
}

.title-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
