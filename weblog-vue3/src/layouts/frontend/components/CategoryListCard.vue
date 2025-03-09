<template>
  <div
    v-if="categories && categories.length > 0"
    class="w-full p-5 mb-3 bg-white border border-gray-200 rounded-lg dark:bg-gray-800 dark:border-gray-700"
  >
    <!-- 灵感标题 -->
    <div class="flex mb-3">
      <h2
        class="flex items-center font-bold text-gray-900 uppercase dark:text-white"
      >
        <!-- 文件夹图标 -->
        <svg
          t="1698998570037"
          class="inline icon w-4 h-4 mr-2"
          viewBox="0 0 1024 1024"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          p-id="21572"
          width="200"
          height="200"
        >
          <path
            d="M938.666667 464.592593h-853.333334v-265.481482c0-62.577778 51.2-113.777778 113.777778-113.777778h128.948148c15.17037 0 28.444444 3.792593 41.718519 11.377778l98.607407 64.474074h356.503704c62.577778 0 113.777778 51.2 113.777778 113.777778v189.62963z"
            fill="#3A69DD"
            p-id="21573"
          ></path>
          <path
            d="M805.925926 398.222222h-587.851852v-125.155555c0-24.651852 20.859259-45.511111 45.511111-45.511111h496.82963c24.651852 0 45.511111 20.859259 45.511111 45.511111V398.222222z"
            fill="#D9E3FF"
            p-id="21574"
          ></path>
          <path
            d="M843.851852 417.185185h-663.703704v-98.607407c0-28.444444 22.755556-53.096296 53.096296-53.096297h559.407408c28.444444 0 53.096296 22.755556 53.096296 53.096297V417.185185z"
            fill="#FFFFFF"
            p-id="21575"
          ></path>
          <path
            d="M786.962963 938.666667h-549.925926c-83.437037 0-151.703704-68.266667-151.703704-151.703704V341.333333s316.681481 37.925926 430.45926 37.925926c189.62963 0 422.874074-37.925926 422.874074-37.925926v445.62963c0 83.437037-68.266667 151.703704-151.703704 151.703704z"
            fill="#5F7CF9"
            p-id="21576"
          ></path>
          <path
            d="M559.407407 512h-75.851851c-20.859259 0-37.925926-17.066667-37.925926-37.925926s17.066667-37.925926 37.925926-37.925926h75.851851c20.859259 0 37.925926 17.066667 37.925926 37.925926s-17.066667 37.925926-37.925926 37.925926z"
            fill="#F9D523"
            p-id="21577"
          ></path>
        </svg>
        灵感
      </h2>
      <span class="grow"></span>

      <!-- 查看更多 -->
      <a
        @click="router.push('/category/list')"
        class="cursor-pointer bg-gray-100 flex items-center px-2.5 py-1 text-xs font-medium text-center text-gray-900 rounded-lg hover:bg-gray-200 focus:ring-4 focus:outline-none focus:ring-gray-200 dark:bg-gray-800 dark:text-white dark:border dark:border-gray-700 dark:hover:bg-gray-700 dark:hover:border-gray-700 dark:focus:ring-gray-700"
      >
        <svg
          class="w-[7px] h-[7px] text-gray-400 dark:text-white"
          aria-hidden="true"
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 8 14"
        >
          <path
            stroke="currentColor"
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="m1 13 5.7-5.326a.909.909 0 0 0 0-1.348L1 1"
          />
        </svg>
      </a>
    </div>

    <!-- 灵感列表 - GitHub风格 -->
    <div class="text-sm font-medium text-gray-600 dark:text-white">
      <div
        v-for="(category, index) in categories"
        :key="index"
        class="border-b border-gray-200 dark:border-gray-700 last:border-b-0"
      >
        <a
          @click="goCategoryArticleListPage(category.id, category.name)"
          class="cursor-pointer flex items-center justify-between py-3 px-1 hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
        >
          <div class="flex items-center">
            <!-- 文件夹图标 -->
            <svg
              class="w-4 h-4 mr-2 text-gray-500"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path
                d="M19.5 2h-15A2.5 2.5 0 0 0 2 4.5v11A2.5 2.5 0 0 0 4.5 18h15a2.5 2.5 0 0 0 2.5-2.5v-11A2.5 2.5 0 0 0 19.5 2ZM11 6h5v2h-5V6Zm-5 6a3 3 0 1 1 0-6 3 3 0 0 1 0 6Zm9 4H4.5a.5.5 0 0 1-.5-.5V14c0-1.1.9-2 2-2h1a5 5 0 0 0 5 0h1c1.1 0 2 .9 2 2v1.5a.5.5 0 0 1-.5.5Z"
              />
            </svg>
            <span>{{ category.name }}</span>
          </div>
          <span
            class="inline-flex items-center justify-center px-2 py-1 text-xs font-semibold text-gray-600 bg-gray-100 rounded-full dark:bg-gray-700 dark:text-gray-300"
          >
            {{ category.articlesTotal }}
          </span>
        </a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { getCategoryList } from '@/api/frontend/category'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 跳转灵感文章列表页
const goCategoryArticleListPage = (id, name) => {
  // 跳转时通过 query 携带参数（灵感 ID、灵感名称）
  router.push({ path: '/category/article/list', query: { id, name } })
}

// 所有灵感
const categories = ref([])
// 一次显示的灵感数
const size = ref(10)

getCategoryList({ size: size.value }).then(res => {
  if (res.success) {
    categories.value = res.data
  }
})
</script>
