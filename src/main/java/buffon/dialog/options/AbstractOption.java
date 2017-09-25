package buffon.dialog.options;

import javax.swing.JComponent;

public abstract class AbstractOption<T> {

	protected String name;

	public String getName() {
		return name;
	}

	public abstract JComponent getComponent();

	public abstract T getValue();
}