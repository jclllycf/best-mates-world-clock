# Best Mates

**语言：** [English](../README.md) · [中文](README_zh-CN.md) · [Français](README_fr.md) · [Deutsch](README_de.md) · [Русский](README_ru.md) · [العربية](README_ar.md) · [Türkçe](README_tr.md)

Best Mates 是一个开源 Android 世界时钟桌面小组件编辑器。它专为希望在手机主屏幕上拥有美观、触感细腻的个性化时钟小组件，但又不想学习复杂代码与公式编辑器的用户而设计。

> **状态：v0.5.0 最终里程碑 — 项目开发完成。** Best Mates v0.5.0 是当前开发阶段的完整里程碑版本。包含全新“Daybook”视觉设计语言、29 款原创几何日月天象图标、25 种几何形状（支持 Mist 磨砂质感）、启动器按比例完全贴合渲染、7 种语言完整本地化（含法语及原生从右至左的阿拉伯语排版）、磁吸对齐参考线及深度系统稳定性优化。主动功能开发已正式闭环，项目进入社区驱动维护阶段。

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
- **丰富几何形状与材质：** 4 大类共 25 种几何形状（含 13 款 v0.5.0 新增形状），提供 Mist（磨砂雾面）、Glass（玻璃质感）与 Solid（纯色）效果。
- **开源字体库：** 内置 21 款优质开源西文字体（Fraunces、Figtree、Bricolage Grotesque、JetBrains Mono、Baloo 2 等）以及 14 款系统动态时钟字体。
- **四款完整视觉风格：** 提供 Daybook、Rosewater、Nocturne、Studio 四套完整主题风格，支持浅色、深色及跟随系统外观。
- **离线与在线城市检索：** 内置全球 GeoNames 离线城市库，同时支持通过 Open-Meteo Geocoding 进行多语言实时在线城市搜索。
- **七种语言完整本地化：** 完整支持中文（简体）、英语、法语、德语、俄语、阿拉伯语（原生 RTL 镜像布局）、土耳其语。
- **主流桌面启动器完美适配：** 消除黑边与阴影溢出，在 Samsung One UI 等现代 Android 启动器上实现边缘平齐渲染。
- **隐私优先设计：** 所有配置纯本地存储，绝无用户账户、埋点分析 SDK、广告追踪或多余权限。

*关于未包含功能的说明：* 本版本专注打造极致的本地世界时钟与排版体验。实时天气预报、指针式时钟以及云端同步/文件导入导出属于探索性概念，并非未修复的缺陷，未包含在 v0.5.0 中。

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

> [!WARNING]
> **重要提示：签名冲突与数据防丢失警告**
> - Best Mates v0.5.0 采用 Debug 证书签名发布。Android 系统安全机制要求同名应用的覆盖升级必须具有完全一致的签名私钥。若设备上已安装早期测试版（如 v0.1.0 或 v0.2.0），系统会阻止直接覆盖安装。
> - **切勿为了解决签名冲突而轻易卸载旧版本！**
> - Best Mates 的所有项目均保存在手机本地的应用私有存储中，目前尚无完整的文件导入/导出备份功能。**卸载应用会彻底删除本地所有已有的小组件工程。**
> - 如果你的手机上已有正在使用的早期版本且需要保留已有配置，请务必保留当前安装，切勿随意卸载，直至后续有可靠的迁移或备份工具可用。
> - 对于新用户或没有旧数据需要保留的情况，可直接安装 v0.5.0 APK。

---

## 项目状态与维护策略

Best Mates 在 v0.5.0 版本已圆满实现既定的设计与功能目标，主动工程开发已正式闭环：
- **不再规划新增功能特性。**
- **不再进行视觉系统重构。**
- **不再进行不必要的底层架构调整。**
- **社区化维护：** 仓库的 [GitHub Issues](../../issues) 保持常开，接收社区反馈的严重崩溃、安全漏洞或 Android 新版本兼容性问题。
- **后续更新策略：** 仅在确有严重缺陷或系统兼容性需要时，进行严格评估后的独立维护修复。

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
- **第三方图标：** 包含来自 Lucide、Tabler、Phosphor、Fluent UI、Iconoir、Material Design、Meteocons 的开源图标，详见 [THIRD_PARTY_NOTICES.md](../THIRD_PARTY_NOTICES.md)。

Copyright 2026 Best Mates 贡献者团队。
