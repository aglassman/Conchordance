package com.conchordance.music;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Scale {
	
	private static Map<Integer,List<Note>> CHROMATIC_SCALE;
	
	static {
		CHROMATIC_SCALE = new HashMap<Integer, List<Note>>();
		CHROMATIC_SCALE.put(0, Arrays.asList(new Note(NoteName.C,0)));
		CHROMATIC_SCALE.put(1, Arrays.asList(new Note(NoteName.C,1),new Note(NoteName.D,-1)));
		CHROMATIC_SCALE.put(2, Arrays.asList(new Note(NoteName.D,0)));
		CHROMATIC_SCALE.put(3, Arrays.asList(new Note(NoteName.D,1),new Note(NoteName.E,-1)));
		CHROMATIC_SCALE.put(4, Arrays.asList(new Note(NoteName.E,0)));
		CHROMATIC_SCALE.put(5, Arrays.asList(new Note(NoteName.F,0)));
		CHROMATIC_SCALE.put(6, Arrays.asList(new Note(NoteName.F,1),new Note(NoteName.G,-1)));
		CHROMATIC_SCALE.put(7, Arrays.asList(new Note(NoteName.G,0)));
		CHROMATIC_SCALE.put(8, Arrays.asList(new Note(NoteName.G,1),new Note(NoteName.A,-1)));
		CHROMATIC_SCALE.put(9, Arrays.asList(new Note(NoteName.A,0)));
		CHROMATIC_SCALE.put(10, Arrays.asList(new Note(NoteName.A,1),new Note(NoteName.B,-1)));
		CHROMATIC_SCALE.put(11, Arrays.asList(new Note(NoteName.B,0)));
	}
	
	public enum ScaleTemplate {
		MAJOR(2,2,1,2,2,2,1),
		MINOR(2,1,2,2,1,2,2),
		HARMONIC_MINOR(2,1,2,2,1,3,1);
		
		public final int steps;
		public final int[] stepSequence;
		
		ScaleTemplate(int ... stepSequence)
		{
			this.steps = stepSequence.length;
			this.stepSequence = stepSequence;
		}
	}
	
	public enum Mode {
		IONIAN(0),
		DORIAN(1),
		PHRYGIAN(2),
		LYDIAN(3),
		MIXOLYDIAN(4),
		AEOLIAN(5),
		LOCRIAN(6);
		
		public final int transposition;
		
		Mode(int transposition)
		{
			this.transposition = transposition;
		}
	}
	
	
	
	
	public static Scale getMajorScale(Note root) {
		return new Scale(root,ScaleTemplate.MAJOR,Mode.IONIAN);
	}
	
	public static Scale getMajorScale(Note root, Mode mode) {
		return new Scale(root,ScaleTemplate.MAJOR,mode);
	}
	
	public static Scale getRelativeMinor(Note root) {
		return new Scale(root,ScaleTemplate.MAJOR,Mode.AEOLIAN);
	}
	
	public static Scale getMinorScale(Note root) {
		return new Scale(root,ScaleTemplate.MINOR,Mode.IONIAN);
	}
	
	public static Scale getMinorScale(Note root, Mode mode) {
		return new Scale(root,ScaleTemplate.MINOR,mode);
	}
	
	public static Scale getHarmonicMinorScale(Note root) {
		return new Scale(root,ScaleTemplate.HARMONIC_MINOR,Mode.IONIAN);
	}
	
	public static Scale getHarmonicMinorScale(Note root, Mode mode) {
		return new Scale(root,ScaleTemplate.HARMONIC_MINOR,mode);
	}
	
	private Scale(Note root, ScaleTemplate scaleTemplate, Mode mode) {
		
		Note[] generatedScale = new Note[7];
		
		Integer semitones = root.halfSteps;
		
		for(int scaleIndex = 0; scaleIndex < scaleTemplate.steps; scaleIndex++) {
			generatedScale[scaleIndex] = determineNote(root, semitones%12,semitones/12);			
			semitones += scaleTemplate.stepSequence[scaleIndex];
		}
		
		generatedScale = transpose(generatedScale,mode);
		
		notes = generatedScale;
	}

	/**
	 * Determine the next note in the scale.
	 * 
	 * If root note is flat, all notes with shared names will use flat.
	 * This is probably incorrect theory wise, as this actually depends on what key the scale is being played in.
	 * 
	 * @param root The root note of the scale.
	 * @param semitones The number of semitones from C
	 * @param octave  The current octave
	 * @return
	 */
	private Note determineNote(Note root, Integer semitones, Integer octave) {
		Integer sharedNoteNameIndex = 0;
		
		if(root.modifier < 0 && CHROMATIC_SCALE.get(semitones).size() > 1)
			sharedNoteNameIndex = 1;
		
		Note foundNote = CHROMATIC_SCALE.get(semitones).get(sharedNoteNameIndex);
		Note trueScaleNote = new Note(foundNote.noteName, foundNote.modifier, octave);
		
		return trueScaleNote;
	}

	/**
	 * Transpose the scaleNotes array by the supplied Mode offset.
	 * 
	 * @param scaleNotes
	 * @param mode
	 * @return
	 */
	private static Note[] transpose(Note[] scaleNotes, Mode mode) {
		if(mode.transposition > 0) {
			Note[] transposedScaleNotes = new Note[scaleNotes.length];
			Note[] subTransposedNotes1 = Arrays.copyOfRange(scaleNotes, mode.transposition, scaleNotes.length);
			Note[] subTransposedNotes2 = Arrays.copyOfRange(scaleNotes, 0, mode.transposition);
			System.arraycopy(subTransposedNotes1, 0, transposedScaleNotes, 0, subTransposedNotes1.length);
			System.arraycopy(subTransposedNotes2, 0, transposedScaleNotes, scaleNotes.length - mode.transposition, subTransposedNotes2.length);
			return fixOctaves(transposedScaleNotes);
		}
		return scaleNotes;
	}
	
	/**
	 * Due to the way the note class works, octaves are based on C.  When a mode
	 * is used to transpose a scale, the note octaves are not in the correct order.
	 * 
	 * This method will resolve any problems with the note octave after transposition.
	 * 
	 * @param modulatedScaleNotes
	 * @return modulatedScaleNotes with the correct octave values
	 */
	private static Note[] fixOctaves(Note[] modulatedScaleNotes) {
		Note[] fixedOctaveScaleNotes = new Note[modulatedScaleNotes.length];
		
		int octave = 0;		

		for(int i = 0; i < modulatedScaleNotes.length; i++)	{	
			Note currentNote = modulatedScaleNotes[i];
			
			if( i > 0 && currentNote.noteName == NoteName.C)
				octave++;
			
			fixedOctaveScaleNotes[i] = new Note(currentNote.noteName,currentNote.modifier,octave);
		}
		return fixedOctaveScaleNotes;
	}

	public String toString() {
		String str = "";
		for (int i = 0; i<notes.length-1; ++i)
			str += notes[i] + ", ";
		str += notes[notes.length-1];
		return str;
	}
	
	public Note[] applyIntervals(Interval... ints) {
		Note[] notes = new Note[ints.length];
		for (int i = 0; i<ints.length; ++i)
			notes[i] = applyInterval(ints[i]);
		return notes;
	}
	
	public Note applyInterval(Interval interval) {
		return notes[interval.major].modify(interval.modifier);
	}
	
	public Note getNote(int majorInterval) {
		return notes[majorInterval - 1];
	}
	
	public Note[] scaleNotes()
	{
		return notes.clone();
	}
	
	private Note[] notes;
}
