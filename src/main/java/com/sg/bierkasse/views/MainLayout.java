package com.sg.bierkasse.views;


import com.sg.bierkasse.views.billentries.AbrechnungView;
import com.sg.bierkasse.views.exportviews.PDFCreator;
import com.sg.bierkasse.views.overviews.BillOverview;
import com.sg.bierkasse.views.billentries.EinzahlungView;
import com.sg.bierkasse.views.overviews.RechnungView;
import com.sg.bierkasse.views.usermanagement.NeueNutzerView;
import com.sg.bierkasse.views.overviews.Overview;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

	private H2 viewTitle;

	public MainLayout() {
		setPrimarySection(Section.DRAWER);
		addDrawerContent();
		addHeaderContent();
	}

	private void addHeaderContent() {
		DrawerToggle toggle = new DrawerToggle();
		toggle.setAriaLabel("Menu toggle");

		viewTitle = new H2();
		viewTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.Margin.NONE);

		addToNavbar(true, toggle, viewTitle);
	}

	private void addDrawerContent() {
		H1 appName = new H1("Bierkasse v2.0");
		appName.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.Margin.NONE);
		Header header = new Header(appName);

		Scroller scroller = new Scroller(createNavigation());

		addToDrawer(header, scroller, createFooter());
	}

	private SideNav createNavigation() {
		SideNav nav = new SideNav();

		nav.addItem(new SideNavItem("Abrechnung", AbrechnungView.class));
		nav.addItem(new SideNavItem("Einzahlung", EinzahlungView.class));
		nav.addItem(new SideNavItem("Neue Nutzer", NeueNutzerView.class));
		nav.addItem(new SideNavItem("Kassenstand", Overview.class));
		nav.addItem(new SideNavItem("Konto", BillOverview.class));
		nav.addItem(new SideNavItem("Rechnungen", RechnungView.class));
		nav.addItem(new SideNavItem("Bericht", PDFCreator.class));

		return nav;
	}

	private Footer createFooter() {
		return new Footer();
	}

	@Override
	protected void afterNavigation() {
		super.afterNavigation();
		viewTitle.setText(getCurrentPageTitle());
	}

	private String getCurrentPageTitle() {
		PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
		return title == null ? "" : title.value();
	}
}