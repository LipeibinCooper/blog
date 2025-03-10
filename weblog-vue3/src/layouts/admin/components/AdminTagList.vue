<template>
  <div>
    <!-- 左边：标签导航栏 -->
    <div
      class="fixed top-[64px] h-[44px] px-2 right-0 z-50 flex items-center bg-white transition-all duration-300 tag-list-container"
      :style="{ left: menuStore.menuWidth }"
    >
      <el-tabs
        v-model="activeTab"
        type="card"
        class="demo-tabs"
        @tab-remove="removeTab"
        @tab-change="tabChange"
        style="min-width: 10px"
      >
        <el-tab-pane
          v-for="item in tabList"
          :key="item.path"
          :label="item.title"
          :name="item.path"
          :closable="item.path != '/admin/index'"
        >
        </el-tab-pane>
      </el-tabs>

      <!-- 右侧下拉菜单 -->
      <span class="ml-auto flex items-center justify-center h-[32px] w-[32px]">
        <el-dropdown @command="handleCloseTab">
          <span
            class="el-dropdown-link cursor-pointer hover:text-primary p-1 rounded transition-all duration-200"
          >
            <el-icon>
              <arrow-down />
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="closeOthers"
                >关闭其他</el-dropdown-item
              >
              <el-dropdown-item command="closeAll">关闭全部</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </span>
    </div>
    <div class="h-[44px]"></div>
  </div>
</template>

<script setup>
import { useTabList } from '@/composables/useTagList.js'

const { menuStore, activeTab, tabList, tabChange, removeTab, handleCloseTab } =
  useTabList()
</script>

<style>
.tag-list-container {
  box-shadow: none;
  border-bottom: 1px solid #f0f0f0;
}

.el-tabs__item {
  font-size: 13px;
  border: none !important;
  border-radius: 4px !important;
  transition: all 0.3s;
  margin: 0 2px;
}

.el-tabs--card > .el-tabs__header .el-tabs__item {
  margin-left: 0.2rem !important;
  margin-right: 0.2rem !important;
}

.el-tabs__item.is-active {
  background-color: #f0f7ff !important;
  color: var(--el-color-primary);
  border-color: transparent !important;
  box-shadow: none;
}

.el-tabs__item.is-active::before {
  display: none;
}

.el-tabs__item:not(.is-active):hover {
  color: var(--el-color-primary);
  background-color: #f5f7fa;
}

.el-tabs {
  height: 32px;
}

.el-tabs__header {
  margin-bottom: 0;
}

.el-tabs--card > .el-tabs__header .el-tabs__nav {
  border: 0;
}

.el-tabs--card > .el-tabs__header .el-tabs__item {
  height: 32px;
  line-height: 32px;
  border: 0;
  background: #fff;
}

.el-tabs--card > .el-tabs__header {
  border: 0;
}

.el-tabs__nav-prev,
.el-tabs__nav-next {
  line-height: 35px;
}

.is-disabled {
  cursor: not-allowed;
  color: #d1d5db;
}

.hover\:text-primary:hover {
  color: var(--el-color-primary);
}
</style>
