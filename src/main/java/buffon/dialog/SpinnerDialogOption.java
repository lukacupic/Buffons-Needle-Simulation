package buffon.dialog;

import javax.swing.JSpinner;

public class SpinnerDialogOption extends AbstractDialogOption<Integer> {

	private JSpinner spinner;

	public SpinnerDialogOption(String name, JSpinner spinner) {
		this.name = name;
		this.spinner = spinner;

		spinner.addChangeListener(e -> System.out.println(name + " changed to: " + getValue()));
	}

	@Override
	public Integer getValue() {
		return (int) (spinner.getValue());
	}

	@Override
	public JSpinner getComponent() {
		return spinner;
	}
}
