# Best Mates

**语言：** [English](../README.md) · [中文](README_zh-CN.md) · [Русский](README_ru.md) · [العربية](README_ar.md) · [Türkçe](README_tr.md) · [Deutsch](README_de.md)

Best Mates 是一个开源 Android 世界时钟桌面小组件编辑器。它专为希望在手机主屏幕上拥有美观、触感细腻的个性化时钟小组件，但又不想学习复杂代码与公式编辑器的用户而设计。

> **状态：v0.5.0 正式发布。** Best Mates v0.5.0 是当前开发阶段的完整里程碑版本。包含全新“Daybook”视觉设计语言、29 款原创几何日月天象图标、13 种支持 Mist 磨砂质感的新形状、启动器按比例完全贴合渲染、6 种语言完整本地化（含原生从右至左的阿拉伯语排版）、磁吸对齐参考线及深度系统适配与稳定性优化。

---

## 视觉画廊

| 桌面小组件效果 | 画布编辑器 (4×2) |
| :---: | :---: |
| ![桌面小组件效果](../docs/review/2026-10-15/after-home-widgets.jpg) | ![画布编辑器](../docs/review/2026-10-15/after-editor-4x2-selected.jpg) |

| 天象图标选择器 | 几何形状库 |
| :---: | :---: |
| ![天象图标选择器](../docs/review/2026-10-15/after-sky-picker.jpg) | ![几何形状库](../docs/review/2026-10-15/after-shape-gallery.jpg) |

| 主题风格系统 (Daybook / Rosewater / Nocturne / Studio) | 阿拉伯语 RTL 原生排版 |
| :---: | :---: |
| ![主题风格系统](../docs/review/2026-10-15/styles-workspace.jpg) | ![阿拉伯语排版](../docs/review/2026-10-15/arabic-rtl.jpg) |

---

## 当前支持功能

- **多小组件项目管理：** 在直观的 Workspace 画廊中独立创建、复制、重命名与编辑多个小组件配置。
- **等比例画布编辑：** 编辑画布直接按目标小组件网格比例呈现（支持 1×1、2×1、2×2、3×3、4×1、4×2、4×3、4×4）。
- **磁吸对齐与层级点选：** 支持边缘、中心、三分线、四分线及图层间磁吸对齐；支持在选中形状背景时直接一键点选上方遮盖的城市或文本。
- **Daybook 原创天象图标：** 29 款纯几何绘制的太阳与月相图标，根据城市精确地理坐标自动计算并呈现当地昼夜。
- **丰富几何形状与材质：** 4 大类 13 种几何形状，提供 Mist（磨砂雾面）、Glass（玻璃质感）与 Solid（纯色）效果。
- **开源字体库：** 内置 21 款优质开源西文字体（Fraunces、Figtree、Bricolage Grotesque、JetBrains Mono、Baloo 2 等）以及 14 款系统动态时钟字体。
- **四款完整视觉风格：** 提供 Daybook、Rosewater、Nocturne、Studio 四套完整主题风格，支持浅色、深色及跟随系统外观。
- **离线与在线城市检索：** 内置全球 GeoNames 离线城市库，同时支持通过 Open-Meteo Geocoding 进行多语言实时在线城市搜索。
- **多语言完整本地化：** 完整支持中文（简体）、英语、俄语、阿拉伯语（原生 RTL 镜像布局）、土耳其语、德语。
- **主流桌面启动器完美适配：** 消除黑边与阴影溢出，在 Samsung One UI 等现代 Android 启动器上实现边缘平齐渲染。
- **隐私优先设计：** 所有配置纯本地存储，绝无用户账户、埋点分析 SDK、广告追踪或多余权限。

*关于未包含功能的说明：* 本阶段专注打造极致的本地世界时钟与排版体验。实时天气预报、指针式时钟以及云端同步/文件导入导出属于探索性概念，并未包含在 v0.5.0 中。

---

## 下载与安装

从本仓库的 [Releases](../../releases) 页面下载 **`BestMates-v0.5.0-debug.apk`**。

- **文件名：** `BestMates-v0.5.0-debug.apk`
- **文件大小：** 约 35.0 MB (35,066,983 字节)
- **SHA-256 校验码：** `7BE893961D26D839D7CF7C2B0093825D0F008AE785462947BC7F595BBF2C4B03`

### 安装步骤

1. 将 APK 文件直接下载至 Android 设备。
2. 根据系统提示，允许浏览器或文件管理器安装来自未知来源的应用。
3. 打开 **Best Mates**，制作或调整你的小组件，点击 **Add to Home**。
4. 若系统启动器未自动弹出固定提示，可长按桌面空白处，从小组件列表中手动拖出 Best Mates。

> [!IMPORTANT]
> **签名与升级兼容性提示：**
> Best Mates v0.5.0 采用 Debug 证书签名发布。Android 安全机制要求同名应用的覆盖升级必须具有完全一致的签名私钥。由于更早期的测试版本（如 v0.1.0 或 v0.2.0）由不同开发机生成，直接覆盖安装可能会提示“安装包无效”。
> 若手机上已有早期测试版，建议在升级前记下你的自定义城市排版，先卸载旧版本后再安装 v0.5.0。

---

## 源码编译

编译需要 Android SDK Platform 35 及 JDK 17。

```powershell
git clone https://github.com/jclllycf/best-mates-world-clock.git
cd best-mates-world-clock

.\gradlew.bat testDebugUnitTest lintDebug assembleDebug
```

编译出的 APK 位于 `app/build/outputs/apk/debug/app-debug.apk`。

---

## 鸣谢与开源许可

- **源代码：** 遵循 [Apache License 2.0](LICENSE)。
- **地理数据：** 内置城市与时区数据来源于 [GeoNames](https://www.geonames.org/)，遵循 [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/) 许可。
- **在线地理编码：** 由 [Open-Meteo Geocoding API](https://open-meteo.com/en/docs/geocoding-api) 提供支持。
- **内置字体：** 遵循 [SIL Open Font License 1.1](http://scripts.sil.org/OFL) 或 Apache 2.0。
- **第三方图标：** 包含来自 Lucide、Tabler、Phosphor、Fluent UI、Iconoir、Material Design、Meteocons 的开源图标，详见 [app/src/main/assets/THIRD_PARTY_NOTICES.md](../app/src/main/assets/THIRD_PARTY_NOTICES.md)。

Copyright 2026 Best Mates 贡献者团队。
