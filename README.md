[![Build Status](https://travis-ci.org/sczerwinski/android-charts.svg?branch=develop)](https://travis-ci.org/sczerwinski/android-charts)

# Android Charts

---

[![Sonatype Snapshot](https://img.shields.io/nexus/s/https/oss.sonatype.org/it.czerwinski.android/charts-core.svg)](https://oss.sonatype.org/content/repositories/snapshots/it/czerwinski/android/charts-core/)

## Core

```groovy
dependencies {
    implementation "it.czerwinski.android:charts-core:$android_charts_version"
}
```

---

[![Sonatype Snapshot](https://img.shields.io/nexus/s/https/oss.sonatype.org/it.czerwinski.android/charts-piechart.svg)](https://oss.sonatype.org/content/repositories/snapshots/it/czerwinski/android/charts-piechart/)

## Pie Charts

```groovy
dependencies {
    implementation "it.czerwinski.android:charts-piechart:$android_charts_version"
}
```

```xml
<it.czerwinski.android.charts.piechart.PieChart
    android:id="@+id/pieChart"
    android:gravity="center"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

### Pie Chart

```xml
<style name="AppTheme">
    <item name="pieChartStyle">@style/AndroidCharts.PieChart.Simple.LightTheme</item>
</style>
```

### Donut Chart

```xml
<style name="AppTheme">
    <item name="pieChartStyle">@style/AndroidCharts.PieChart.Donut.LightTheme</item>
</style>
```
