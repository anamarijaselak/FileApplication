import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

/**
 * Klasa koja predstavlja aplikaciju. Korisniku omogućuje unos putanje do
 * direktorija, pregled datoteka u zadanom direktoriju i odabir favoritaunutar
 * istog direktorija. Korisnik može mijenjati putanju do direktorija, kao i
 * favorite. Nakon gašenja i ponovnog podizanja aplikacije korisniku se
 * prikazuje zadnje nešena putanja i odabrani favoriti.
 * 
 * @author Ana Marija Selak
 *
 */
public class Application extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	static List<String> hierarchy = new ArrayList<String>();
	static List<String> favouriteFiles = new ArrayList<String>();
	static List<String> files = new ArrayList<String>();
	static List<String> newFavourites = new ArrayList<String>();

	public Application() {

		setTitle(" Choose Favourites ");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setSize(600, 600);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		initGui();

	}

	private void initGui() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel everything = new JPanel();
		everything.setLayout(new BoxLayout(everything, BoxLayout.Y_AXIS));

		JPanel favourites = new JPanel();
		favourites.setLayout(new BoxLayout(favourites, BoxLayout.Y_AXIS));

		JButton changePath = new JButton("Change path");

		JButton addFavourites = new JButton("Add selected files to favourites");

		showFavourites(favourites);
		List<JTree> trees = new ArrayList<JTree>();
		trees.add(showHierarchy(everything));

		addFavourites.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (trees.get(trees.size() - 1) != null) {

					int[] rows = trees.get(trees.size() - 1).getSelectionRows();

					for (int i = 0; i < rows.length; i++) {
						// odabran je list stabla - datoteka

						if (rows[i] >= hierarchy.size()
								&& !favouriteFiles.contains(files.get(rows[i]
										- hierarchy.size()))) {
							favouriteFiles.add(files.get(rows[i]
									- hierarchy.size()));

						}
					}

					DataBaseOperations.writeFavourites("Favourites.txt",
							favouriteFiles);

				}

				favourites.removeAll();
				showFavourites(favourites);
				favourites.revalidate();

			}
		});

		changePath.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String path = JOptionPane
						.showInputDialog(null, "Enter absolute path to a file",
								"Which path do you want?",
								JOptionPane.QUESTION_MESSAGE);
				if (path != null) {
					String msg = DataBaseOperations.parsePath(path,
							favouriteFiles, hierarchy, files);
					if (msg != "OK") {
						JOptionPane.showMessageDialog(null, msg,
								"Try another path!",
								JOptionPane.WARNING_MESSAGE);
					}

					favourites.removeAll();
					everything.removeAll();

					showFavourites(favourites);
					trees.add(showHierarchy(everything));

					favourites.revalidate();
					everything.revalidate();
				}

			}

		});

		showMainPanel(mainPanel, everything, favourites, changePath,
				addFavourites);

		setBorders(everything, favourites, changePath, addFavourites);

		getContentPane().add(mainPanel);

	}

	private void setBorders(JPanel everything, JPanel favourites,
			JButton changePath, JButton addFavourites) {
		everything.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		favourites.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		changePath.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		addFavourites.setBorder(BorderFactory.createLineBorder(Color.BLACK));

	}

	private void showMainPanel(JPanel mainPanel, JPanel everything,
			JPanel favourites, JButton changePath, JButton addFavourites) {
		mainPanel.add(everything, BorderLayout.CENTER);
		mainPanel.add(favourites, BorderLayout.WEST);
		mainPanel.add(changePath, BorderLayout.NORTH);
		mainPanel.add(addFavourites, BorderLayout.SOUTH);

	}

	private void showFavourites(JPanel favourites) {
		favourites.add(new JLabel("Favourites"));
		if (favouriteFiles.size() != 0) {
			String[] items = new String[favouriteFiles.size()];
			int i = 0;
			for (String item : favouriteFiles) {

				items[i] = item;
				i++;
			}

			JList<String> favItemsComponent = new JList<String>(items);

			favourites.add(new JScrollPane(favItemsComponent));
		}

	}

	private JTree showHierarchy(JPanel everything) {
		everything.add(new JLabel("Whole path"));
		if (hierarchy.size() != 0) {

			List<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
			for (int i = 0; i < hierarchy.size(); i++) {
				nodes.add(new DefaultMutableTreeNode(hierarchy.get(i)));

			}
			for (int i = 0; i < hierarchy.size() - 1; i++) {
				nodes.get(i).add(nodes.get(i + 1));

			}

			if (files.size() != 0) {
				for (int i = 0; i < files.size(); i++) {
					nodes.get(nodes.size() - 1).add(
							new DefaultMutableTreeNode(files.get(i)));
				}
			} else {
				nodes.get(nodes.size() - 1).add(new DefaultMutableTreeNode());
			}
			JTree tree = new JTree(nodes.get(0));
			tree.getSelectionModel().setSelectionMode(
					TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

			JScrollPane treeView = new JScrollPane(tree);
			everything.add(treeView);
			return tree;

		}
		return null;

	}

	public static void main(String[] args) throws IOException {

		hierarchy = DataBaseOperations.getHierarchy("Hierarchy.txt");
		favouriteFiles = DataBaseOperations.getFavourites("Favourites.txt");
		files = DataBaseOperations.getFiles("Files.txt");
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Application().setVisible(true);
			}
		});
	}

}
