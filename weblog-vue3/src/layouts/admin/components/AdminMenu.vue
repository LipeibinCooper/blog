<template>
  <div
    class="fixed overflow-y-auto menu-container transition-all duration-300"
    :style="{ width: menuStore.menuWidth }"
  >
    <!-- 顶部 Logo, 指定高度为 64px, 和右边的 Header 头保持一样高 -->
    <div class="flex items-center justify-center h-[64px] logo-container">
      <img
        v-if="menuStore.menuWidth == '250px'"
        src="@/assets/weblog-logo.png"
        class="h-[60px] transition-all duration-300"
      />
      <img
        v-else
        src="@/assets/weblog-logo-mini.png"
        class="h-[30px] transition-all duration-300"
      />
    </div>

    <!-- 下方菜单 -->
    <el-menu
      :default-active="defaultActive"
      @select="handleSelect"
      :collapse="isCollapse"
      :collapse-transition="false"
      class="menu-list"
    >
      <el-menu-item
        v-for="(item, index) in menus"
        :key="index"
        :index="item.path"
        class="menu-item"
      >
        <el-icon class="menu-icon">
          <!-- 动态图标 -->
          <component :is="item.icon"></component>
        </el-icon>
        <span class="menu-text">{{ item.name }}</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useMenuStore } from '@/stores/menu'

const menuStore = useMenuStore()

const route = useRoute()
const router = useRouter()

// 是否折叠
const isCollapse = computed(() => !(menuStore.menuWidth == '250px'))

// 根据路由地址判断哪个菜单被选中
const defaultActive = ref(route.path)

// 菜单选择事件
const handleSelect = path => {
  router.push(path)
}

const menus = [
  {
    name: '仪表盘',
    icon: 'Monitor',
    path: '/admin/index'
  },
  {
    name: '灵感节点管理',
    icon: 'Document',
    path: '/admin/article/list'
  },
  {
    name: '灵感管理',
    icon: 'FolderOpened',
    path: '/admin/category/list'
  },
  {
    name: '标签管理',
    icon: 'PriceTag',
    path: '/admin/tag/list'
  },
  {
    name: '知识库管理',
    icon: 'Collection',
    path: '/admin/wiki/list'
  },
  {
    name: '评论管理',
    icon: 'ChatDotSquare',
    path: '/admin/comment/list'
  },
  {
    name: '博客设置',
    icon: 'Setting',
    path: '/admin/blog/settings'
  }
]
</script>

<style>
.menu-container {
  background: #ffffff;
  color: #333;
  height: 100vh;
  z-index: 1000;
  border-right: 1px solid #f0f0f0;
}

.logo-container {
  border-bottom: 1px solid #f0f0f0;
}

.menu-list {
  background: transparent;
  border-right: 0;
  margin-top: 10px;
}

.menu-item {
  margin: 5px 10px;
  border-radius: 8px;
  height: 50px;
  line-height: 50px;
  transition: all 0.3s;
}

.menu-icon {
  margin-right: 10px;
  font-size: 18px;
  color: #666;
}

/* 收起状态下的菜单项样式 */
.el-menu--collapse .menu-item {
  display: flex;
  justify-content: center;
  padding: 0 !important;
}

/* 收起状态下的图标样式 */
.el-menu--collapse .menu-icon {
  margin: 0;
}

.menu-text {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.el-menu {
  background-color: transparent;
  border-right: 0;
}

.el-sub-menu__title {
  color: #333;
}

.el-sub-menu__title:hover {
  background-color: #f5f7fa;
  border-radius: 8px;
}

.el-menu-item.is-active {
  background-color: #f0f7ff;
  color: var(--el-color-primary);
  border-radius: 8px;
  font-weight: 500;
}

.el-menu-item.is-active .menu-icon {
  color: var(--el-color-primary);
}

.el-menu-item.is-active:before {
  display: none;
}

.el-menu-item {
  color: #333;
}

.el-menu-item:hover {
  background-color: #f5f7fa;
  color: #333;
  border-radius: 8px;
}
</style>
