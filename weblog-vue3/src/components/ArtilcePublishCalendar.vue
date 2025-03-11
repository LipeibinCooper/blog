<template>
    <!-- 日历热点图容器 -->
    <div id="calendar" class="overflow-x-auto w-full h-72"></div>
</template>

<script setup>
import * as echarts from 'echarts'
import { watch, onMounted, onUnmounted, ref } from 'vue'
import { format, subMonths, subYears } from 'date-fns'

// 对外暴露的属性值
const props = defineProps({
    value: { // 属性值名称
        type: Object, // 类型为对象
        default: null // 默认为 null
    }
})

// 保存图表实例的引用
const chartInstance = ref(null)

// 当前日期
const currentDate = new Date();
// 一年前
const oneYearAgo = subYears(currentDate, 1)

// 格式化后的开始、结束日期
const startDate = format(oneYearAgo, 'yyyy-MM-dd')
const endDate = format(currentDate, 'yyyy-MM-dd')

// 日历热点数据
const myData = []

// 初始化日历热点图
function initCalendar() {
    if (!props.value) return;
    
    // 清空之前的数据
    myData.length = 0;
    
    // 将传入的数据设置到 myDate 数组中
    let map = props.value
    for (let key in map) {
        myData.push([
            key,
            map[key]
        ]);
    }

    var chartDom = document.getElementById('calendar');
    
    // 如果已经有图表实例，先销毁它
    if (chartInstance.value) {
        chartInstance.value.dispose();
    }
    
    // 自适应容器宽度，不再固定宽度
    chartInstance.value = echarts.init(chartDom);
    
    const option = {
        visualMap: {
            show: false,
            min: 0,
            max: 10
        },
        calendar: { // 日历显示的范围：开始日期 - 结束日期
            range: [startDate, endDate],
            itemStyle: {
                borderWidth: 0.5
            },
            yearLabel: {
                show: true
            },
            dayLabel: {
                firstDay: 1, // 从周一开始
                nameMap: 'cn'
            },
            monthLabel: {
                nameMap: 'cn'
            },
            cellSize: ['auto', 20] // 自适应宽度，固定高度
        },
        series: {
            type: 'heatmap',
            coordinateSystem: 'calendar',
            data: myData
        }, 
        gradientColor: [ // 自定义热点颜色，参考了 GitHub 代码提交的颜色
            '#fff',
            '#40c463',
            '#30a14e',
            '#216e39',
        ]
    };

    chartInstance.value.setOption(option);
}

// 处理窗口大小变化
function handleResize() {
    if (chartInstance.value) {
        chartInstance.value.resize();
    }
}

// 组件挂载时初始化图表
onMounted(() => {
    // 如果已经有数据，立即初始化图表
    if (props.value) {
        initCalendar();
    }
    
    // 监听窗口大小变化
    window.addEventListener('resize', handleResize);
});

// 组件卸载时清理
onUnmounted(() => {
    if (chartInstance.value) {
        chartInstance.value.dispose();
        chartInstance.value = null;
    }
    window.removeEventListener('resize', handleResize);
});

// 侦听属性, 监听 props.value 的变化，一旦 props.value 发生变化，就调用 initCalendar 初始化日历热点图
watch(() => props.value, () => initCalendar(), { deep: true })

</script>