package com.qwertee.mobile;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.qwertee.tees.BuyableTee;
import com.qwertee.utilities.NavDrawItem;
import com.qwertee.utilities.NavDrawItem.Type;
import com.qwertee.utilities.NavDrawerAdapter;

public class MainActivity extends ActionBarActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListLeft;

	private LinearLayout linearDrawer;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	public ActionBarDrawerToggle mDrawerToggle;
	private NavDrawerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupDrawer(savedInstanceState);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		FragmentManager manager = getSupportFragmentManager();
		manager.beginTransaction().replace(R.id.content, new VoteFragment())
				.commit();
	}

	private void setupDrawer(Bundle savedInstanceState) {
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerListLeft = (ListView) findViewById(R.id.left_drawer);
		linearDrawer = (LinearLayout) findViewById(R.id.linear_drawer);
		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		adapter = new NavDrawerAdapter(this);
		mDrawerListLeft.setAdapter(adapter);
		addItemsToAdapter();

		mDrawerListLeft.setOnItemClickListener(new DrawerItemClickListener());

		// ActionBarDrawerToggle ties together the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_navigation_drawer, /*
										 * nav drawer image to replace 'Up'
										 * caret
										 */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				// getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				// getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		mDrawerToggle.syncState();
		if (savedInstanceState == null) {
			// selectItem(0);
		}
	}

	private void addItemsToAdapter() {
		adapter.addItem(new NavDrawItem("Tees", Type.CATEGORY));
		adapter.addItem(new NavDrawItem("Today's Tee", Type.ENTRY));
		adapter.addItem(new NavDrawItem("Last Chance Tee", Type.ENTRY));
		adapter.addItem(new NavDrawItem("Previous Tees", Type.ENTRY));
		adapter.addItem(new NavDrawItem("Free Tees", Type.ENTRY));

		adapter.addItem(new NavDrawItem("Vote", Type.CATEGORY));
		adapter.addItem(new NavDrawItem("Vote for Tees", Type.ENTRY));
		adapter.addItem(new NavDrawItem("Submit Design", Type.ENTRY));

		adapter.addItem(new NavDrawItem("Talk", Type.CATEGORY));
		adapter.addItem(new NavDrawItem("Forum", Type.ENTRY));
		adapter.addItem(new NavDrawItem("Blog / News", Type.ENTRY));

		adapter.addItem(new NavDrawItem("Help", Type.CATEGORY));
		adapter.addItem(new NavDrawItem("What is Qwertee?", Type.ENTRY));
		adapter.addItem(new NavDrawItem("Help & FAQ", Type.ENTRY));
		adapter.addItem(new NavDrawItem("Contact us", Type.ENTRY));

		adapter.addItem(new NavDrawItem("Artist Resources", Type.CATEGORY));
	}

	private void selectItem(int position) {
		// update selected item then close the drawer

		FragmentManager manager = getSupportFragmentManager();

		switch (position) {
		case 0:
			manager.beginTransaction()
					.replace(R.id.content, new BuyableTeesViewFragment())
					.commit();
			break;
		case 1: // TODO fix that shit
			TeeFragment fragment = new TodaysTeeFragment();
			manager.beginTransaction().replace(R.id.content, fragment).commit();
			break;
		case 2:
			fragment = new LastChanceTeeFragment();
			manager.beginTransaction().replace(R.id.content, fragment) // TODO
					// combine
					.commit();
			break;
		case 3:
			manager.beginTransaction()
					.replace(R.id.content, new PreviousTeesFragment()).commit();
			break;
		case 6:
			manager.beginTransaction()
					.replace(R.id.content, new VoteFragment()).commit();
			break;
		case 12:
			AboutFragment.newInstance().show(manager, "dialog");
			break;
		case 13:
			manager.beginTransaction()
					.replace(R.id.content, new HelpFragment()).commit();
			break;
		// TODO fading between them

		}

		// .setItemChecked(position, true);
		// setTitle(categories[position]);
		mDrawerLayout.closeDrawer(linearDrawer);
	}

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		/*
		 * If the nav drawer is open, hide action items related to the content
		 * view
		 */
		@SuppressWarnings("unused")
		boolean leftDrawerOpen = mDrawerLayout.isDrawerOpen(linearDrawer);
		// menu.findItem(R.id.action_settings).setVisible(!drawerOpen); //TODO

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onMaximizeClick(BuyableTee tee) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_in_right,
				R.anim.slide_out_left, R.anim.slide_in_left,
				R.anim.slide_out_right); // Add this transaction to the back
		// stack
		transaction
				.replace(R.id.content, new SingleBuyableTeeViewFragment(tee))
				.addToBackStack("");
		transaction.commit();
	}

	@Override
	public void onBackPressed() {
		FragmentManager fm = getSupportFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();
		} else {
			super.onBackPressed();
		}
	}

}
