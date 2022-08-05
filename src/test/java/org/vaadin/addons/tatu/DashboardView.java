package org.vaadin.addons.tatu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.vaadin.addons.tatu.GridLayout.Orientation;
import org.vaadin.addons.tatu.prototools.Dashboard;
import org.vaadin.addons.tatu.prototools.Dashboard.Widget;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataLabels;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.HorizontalAlign;
import com.vaadin.flow.component.charts.model.PlotOptionsPie;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends Div {

    Random random = new Random();
    List<Widget> widgets = new ArrayList<>();
    
    public DashboardView() {
        setSizeFull();
        Dashboard dashboard = new Dashboard(Orientation.BY_COLUMNS,4);
        for (int i=0;i<5;i++) {
            Chart chart = createChart();

            Widget widget = dashboard.addWidget("Chart "+i, chart);
            widgets.add(widget);
        }
        dashboard.addWidgetGenerator(VaadinIcon.CHART.create(), "Chart", Void -> createChart());
        dashboard.addWidgetGenerator(VaadinIcon.TABLE.create(), "Table", Void -> createGrid());

        dashboard.addWidgetUpdater(VaadinIcon.CHART.create(), "Chart", Void -> createChart());
        dashboard.addWidgetUpdater(VaadinIcon.TABLE.create(), "Table", Void -> createGrid());

        dashboard.addWidgetResizedListener(event -> {
           Widget widget = event.getWidget();
           Chart chart = (Chart) widget.getWidgetContent();
           chart.getElement().executeJs("this.__reflow();");
        });
        add(dashboard);
    }

    private Chart createChart() {
        Chart chart = new Chart(ChartType.PIE);
        PlotOptionsPie plotOptions = new PlotOptionsPie();
        Configuration conf = chart.getConfiguration();
        plotOptions.setSize("100%");
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(true);
        dataLabels.setAlign(HorizontalAlign.CENTER);
        dataLabels.setFormatter("function() {return this.percentage.toFixed(0) + '%';}");
        dataLabels.setDistance(-20);
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);
        DataSeries pieSeries = new DataSeries();
        pieSeries.add(new DataSeriesItem("Value 0", random.nextInt(100)));
        pieSeries.add(new DataSeriesItem("Value 1", random.nextInt(100)));
        pieSeries.add(new DataSeriesItem("Value 2", random.nextInt(100)));
        pieSeries.add(new DataSeriesItem("Value 3", random.nextInt(100)));
        pieSeries.add(new DataSeriesItem("Value 4", random.nextInt(100)));
        conf.setSeries(pieSeries);
        return chart;
    }

    private Grid<String> createGrid() {
        Grid<String> grid = new Grid<>();
        grid.addColumn(item -> item).setHeader("Name");
        grid.addColumn(item -> random.nextInt(100)).setHeader("Value");
        grid.setItems("Value 0","Value 1","Value 2", "Value 3", "Value 4");
        return grid;
    }
}
