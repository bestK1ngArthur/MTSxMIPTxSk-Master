package ru.bestk1ng.java.hw3;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import ru.bestk1ng.java.hw3.db.DBWorker;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ChartBuilder {
    private final static String OUTPUT_PATH = "./charts/";

    private static final int chartWidth = 1920;
    private static final int chartHeight = 1080;

    private void configureChart(JFreeChart barChart, String title) {
        CategoryPlot plot = barChart.getCategoryPlot();
        CategoryAxis axis = plot.getDomainAxis();

        Font font = new Font("Cambria", Font.BOLD, 25);
        axis.setTickLabelFont(font);
        Font font3 = new Font("Cambria", Font.BOLD, 30);
        barChart.setTitle(new org.jfree.chart.title.TextTitle(
                title,
                new java.awt.Font("Cambria", java.awt.Font.BOLD, 40)
        ));

        plot.getDomainAxis().setLabelFont(font3);
        plot.getRangeAxis().setLabelFont(font3);
        CategoryPlot categoryPlot = (CategoryPlot) barChart.getPlot();
        BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
    }

    public void buildChard(String title, String xTitle, String yTitle, DBWorker.Chart chart) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < chart.barNames.length; i++) {
            dataset.addValue(chart.values[i].doubleValue(), "default", chart.barNames[i]);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                title,
                xTitle,
                yTitle,
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );
        configureChart(barChart, title);
        File file = new File(OUTPUT_PATH + title + ".png");
        file.getParentFile().mkdirs();

        try {
            file.createNewFile();
            ChartUtilities.saveChartAsPNG(file, barChart, chartWidth, chartHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}